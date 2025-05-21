# src/recommender.py

import logging
from collections import defaultdict
from stores import posts_store, user_interests, post_contents, post_id_to_content_idx
from nlp_model import preprocess_text, TopicModel
from config import NUM_TOPICS, TOPIC_MODEL_UPDATE_INTERVAL_POSTS, REC_GENERATED_TOPIC
from kafka_utils import get_kafka_producer

# Initialize topic model and related counters
topic_model = TopicModel(num_topics=NUM_TOPICS)
posts_since_last_model_update = 0
kafka_producer = get_kafka_producer()


def process_post_created(post_event):
    """Processes a post-created event, assigns a topic, and stores the post."""
    global posts_since_last_model_update # To modify the global counter

    post_id = post_event.get('postId')
    if not post_id:
        logging.warning(f"Received malformed post-created event (missing postId): {post_event}")
        return

    content = post_event.get('content', '')
    preprocessed_content = preprocess_text(content)

    # Store content for future model retraining
    post_contents.append(preprocessed_content)
    post_id_to_content_idx[post_id] = len(post_contents) - 1

    # Retrain model periodically
    posts_since_last_model_update += 1
    if posts_since_last_model_update >= TOPIC_MODEL_UPDATE_INTERVAL_POSTS:
        logging.info(f"Retraining topic model after {posts_since_last_model_update} new posts.")
        topic_model.train(post_contents)
        posts_since_last_model_update = 0 # Reset counter

    # Predict topic for the new post
    topic_label = topic_model.predict_topic(preprocessed_content)

    # Add the generated topic label to the post event and store it
    post_event['topic_label'] = topic_label
    posts_store[post_id] = post_event
    logging.info(f"Processed post-created: Post {post_id} with generated topic '{topic_label}'")
    logging.debug(f"Current posts_store: {posts_store}")


def process_post_liked(like_event):
    """Processes a post-liked event and updates user interests based on post topic."""
    post_id = like_event.get('postId')
    user_id = like_event.get('userId')

    if not post_id or not user_id:
        logging.warning(f"Received malformed post-liked event (missing postId or userId): {like_event}")
        return

    post = posts_store.get(post_id)
    if post and 'topic_label' in post:
        topic_label = post['topic_label']
        user_interests[user_id].add(topic_label)
        logging.info(f"Processed post-liked: User {user_id} liked post {post_id} (topic: '{topic_label}')")
        logging.debug(f"Current user_interests for user {user_id}: {user_interests[user_id]}")
    else:
        logging.warning(f"Post {post_id} not found in store or missing topic for like by user {user_id}. Cannot track interest.")

def generate_recommendations():
    """Generates recommendations and publishes them to Kafka."""
    logging.info("Generating recommendations...")
    recommendations_to_publish = defaultdict(set) # postId -> {user1, user2, ...}

    # Only generate recommendations if the topic model is trained
    if not topic_model.trained:
        logging.warning("Topic model not trained yet. Skipping recommendation generation.")
        return

    # In a real system, you'd filter by timestamp to get truly "recent" posts.
    recent_posts = list(posts_store.values()) # Convert to list to iterate

    if not recent_posts:
        logging.info("No recent posts available for recommendation.")
        return

    for user_id, interests in user_interests.items():
        for post in recent_posts:
            if 'topic_label' in post and post['topic_label'] in interests:
                # Basic recommendation: if user's interest matches post's topic
                recommendations_to_publish[post['postId']].add(user_id)

    for post_id, users in recommendations_to_publish.items():
        if users:
            rec_payload = {"postId": post_id, "users": list(users)}
            try:
                kafka_producer.send(REC_GENERATED_TOPIC, value=rec_payload)
                logging.info(f"Published recommendation: {rec_payload}")
            except Exception as e:
                logging.error(f"Failed to publish recommendation {rec_payload}: {e}")

    logging.info("Recommendation generation complete.")