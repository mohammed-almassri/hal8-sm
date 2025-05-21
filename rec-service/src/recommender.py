# recommendation-service/src/recommender.py

import logging
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import random
import time
import uuid # Import UUID module
from datetime import datetime, timezone # Import datetime for Instant handling

from config import REC_GENERATED_TOPIC, TOPIC_MODEL_UPDATE_INTERVAL_POSTS, NUM_TOPICS
from stores import user_post_store, post_topic_store, topic_model_store, user_vector_store
from nlp_model import train_topic_model, infer_topic
from kafka_utils import get_kafka_producer

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

kafka_producer = get_kafka_producer()

def process_post_created(event_data):
    """
    Processes a PostCreatedEvent.
    Expected schema: { "postId": "...", "userId": "...", "content": "...", "createdAt": "..." }
    """
    try:
        post_id = event_data.get("postId")
        # Convert userId string to UUID object for internal use if necessary
        user_id_str = event_data.get("userId")
        user_id = uuid.UUID(user_id_str) if user_id_str else None

        content = event_data.get("content")
        # Parse ISO 8601 string to datetime object
        created_at_str = event_data.get("createdAt")
        # Ensure it's parsed as UTC and is timezone-aware
        created_at = datetime.fromisoformat(created_at_str.replace('Z', '+00:00')) if created_at_str else datetime.now(timezone.utc)

        if not all([post_id, user_id, content]):
            logging.warning(f"Skipping incomplete PostCreatedEvent: {event_data}")
            return

        logging.info(f"Processing PostCreated: Post ID={post_id}, User ID={user_id}")

        # Store post content for topic modeling
        user_post_store[post_id] = {'userId': user_id, 'content': content}

        # Train topic model if enough new posts are accumulated
        if len(user_post_store) % TOPIC_MODEL_UPDATE_INTERVAL_POSTS == 0:
            logging.info(f"Accumulated {len(user_post_store)} posts. Training topic model...")
            posts_for_training = list(user_post_store.values())
            try:
                trained_model, feature_names = train_topic_model([p['content'] for p in posts_for_training], NUM_TOPICS)
                topic_model_store['model'] = trained_model
                topic_model_store['feature_names'] = feature_names
                logging.info("Topic model trained successfully.")
            except Exception as e:
                logging.error(f"Error training topic model: {e}", exc_info=True)
                # Continue without updating model if training fails

        # Infer topic for the new post if model is available
        if 'model' in topic_model_store and 'feature_names' in topic_model_store:
            try:
                topic_vector = infer_topic(content, topic_model_store['model'], topic_model_store['feature_names'])
                post_topic_store[post_id] = topic_vector
                # Update user's interest vector based on the new post
                current_user_vector = user_vector_store.get(user_id, [0.0] * NUM_TOPICS)
                updated_user_vector = [(current_user_vector[i] + topic_vector[i]) / 2 for i in range(NUM_TOPICS)] # Simple average
                user_vector_store[user_id] = updated_user_vector
                logging.debug(f"User {user_id} vector updated: {updated_user_vector}")
            except Exception as e:
                logging.warning(f"Could not infer topic for post {post_id}: {e}", exc_info=True)

    except Exception as e:
        logging.error(f"Error processing PostCreatedEvent: {event_data}. Error: {e}", exc_info=True)

def process_post_liked(event_data):
    """
    Processes a PostLikedEvent.
    Expected schema: { "postId": "...", "userId": "...", "totalLikes": ..., "likedAt": "..." }
    """
    try:
        post_id = event_data.get("postId")
        # Convert userId string to UUID object
        user_id_str = event_data.get("userId")
        user_id = uuid.UUID(user_id_str) if user_id_str else None
        
        # total_likes = event_data.get("totalLikes") # Not used in current logic, but available
        # liked_at_str = event_data.get("likedAt") # Not used in current logic, but available
        # liked_at = datetime.fromisoformat(liked_at_str.replace('Z', '+00:00')) if liked_at_str else datetime.now(timezone.utc)

        if not all([post_id, user_id]):
            logging.warning(f"Skipping incomplete PostLikedEvent: {event_data}")
            return

        logging.info(f"Processing PostLiked: User ID={user_id}, Post ID={post_id}")

        # Get the topic of the liked post
        liked_post_topic_vector = post_topic_store.get(post_id)
        if liked_post_topic_vector:
            # Update user's interest vector based on liked post's topic
            current_user_vector = user_vector_store.get(user_id, [0.0] * NUM_TOPICS)
            # Simple reinforcement: add the liked post's topic vector
            # a more sophisticated weighted average or decay may be better
            updated_user_vector = [current_user_vector[i] + liked_post_topic_vector[i] for i in range(NUM_TOPICS)]
            user_vector_store[user_id] = updated_user_vector
            logging.debug(f"User {user_id} vector reinforced by like: {updated_user_vector}")
        else:
            logging.warning(f"Liked post {post_id} not found in topic store. Cannot update user {user_id} vector.")

    except Exception as e:
        logging.error(f"Error processing PostLikedEvent: {event_data}. Error: {e}", exc_info=True)

def generate_recommendations(num_recommendations=5): # Added num_recommendations parameter
    """
    Generates recommendations for users based on their interest vectors using cosine similarity.
    """
    if not user_vector_store:
        logging.info("No user interest vectors available for recommendation generation.")
        return

    if not post_topic_store or not topic_model_store.get('model'):
        logging.info("No post topic vectors or trained topic model available for recommendation generation.")
        return

    logging.info("Generating recommendations using cosine similarity...")
    recommendations_published = 0

    all_post_ids = list(post_topic_store.keys())
    if not all_post_ids:
        logging.info("No posts with inferred topics available for recommendations.")
        return

    # Convert all post topic vectors to a NumPy array for efficient calculation
    # Ensure all topic vectors have the same dimension (NUM_TOPICS)
    try:
        all_post_vectors = np.array([post_topic_store[pid] for pid in all_post_ids if len(post_topic_store[pid]) == NUM_TOPICS])
        all_post_ids_filtered = [pid for pid in all_post_ids if len(post_topic_store[pid]) == NUM_TOPICS]
    except ValueError as e:
        logging.error(f"Error converting post topic vectors to NumPy array: {e}. Check vector dimensions.")
        return

    if not all_post_ids_filtered:
        logging.info("No valid post topic vectors to compare against.")
        return

    for user_id, user_vector in user_vector_store.items():
        # Convert user_vector to numpy array and reshape for cosine_similarity
        user_vector_np = np.array(user_vector).reshape(1, -1) # Reshape to (1, NUM_TOPICS)

        if user_vector_np.sum() == 0: # Check if user vector is all zeros (no interests yet)
            logging.debug(f"Skipping recommendation for user {user_id} due to zero vector.")
            continue

        # Calculate cosine similarity between the user's vector and all post vectors
        # Result will be an array of similarities, one for each post
        similarities = cosine_similarity(user_vector_np, all_post_vectors)[0] # [0] to get the 1D array

        # Pair similarities with post IDs
        post_similarities = list(zip(all_post_ids_filtered, similarities))

        # Sort posts by similarity in descending order
        post_similarities.sort(key=lambda x: x[1], reverse=True)

        # Get the top N recommendations
        recommended_posts_for_user = [pid for pid, sim in post_similarities[:num_recommendations]]

        if recommended_posts_for_user:
            recommendation_event = {
                "userId": str(user_id), # Convert UUID back to string for JSON serialization
                "recommendedPostIds": recommended_posts_for_user,
                "timestamp": int(time.time() * 1000)
            }
            try:
                kafka_producer.send(REC_GENERATED_TOPIC, value=recommendation_event)
                logging.info(f"Published recommendation for user {user_id}: {recommendation_event['recommendedPostIds']}")
                recommendations_published += 1
            except Exception as e:
                logging.error(f"Failed to publish recommendation for user {user_id}: {e}", exc_info=True)
        else:
            logging.info(f"No recommendations generated for user {user_id} this cycle.")

    if recommendations_published > 0:
        logging.info(f"Finished generating. Published {recommendations_published} recommendations.")
    else:
        logging.info("No recommendations published this cycle.")