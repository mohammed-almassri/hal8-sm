import json
import time
import random
import pandas as pd
from kafka import KafkaProducer
import os

# IMPORTANT: For local testing with Dockerized Kafka, use the host port exposed by Kafka.
#KAFKA_BOOTSTRAP_SERVERS = 'localhost:29092' # Ensure this matches your Kafka setup

KAFKA_BOOTSTRAP_SERVERS = os.getenv("KAFKA_BOOTSTRAP_SERVERS", "localhost:29092")

POST_CREATED_TOPIC = 'post-created'
POST_LIKED_TOPIC = 'post-liked'

producer = KafkaProducer(
    bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
    value_serializer=lambda x: json.dumps(x).encode('utf-8')
)

# Configuration for publisher behavior (can be moved to config.py if desired)
INITIAL_POSTS_FOR_TRAINING = 30
BATCH_SIZE = 10
LIKES_PER_BATCH = 7
TOTAL_POSTS_TO_PUBLISH = 10000
DELAY_BETWEEN_EVENTS = 1
DELAY_BETWEEN_BATCHES = 5


def _get_producer(kafka_bootstrap_servers):
    global producer
    if producer is None:
        producer = KafkaProducer(
            bootstrap_servers=kafka_bootstrap_servers,
            value_serializer=lambda x: json.dumps(x).encode('utf-8')
        )
    return producer

def _publish_post_created_event(kafka_bootstrap_servers, post_id, user_id, content):
    prod = _get_producer(kafka_bootstrap_servers)
    event = {
        "postId": post_id,
        "userId": user_id,
        "content": content,
        "timestamp": int(time.time() * 1000)
    }
    prod.send("post-created", value=event) # Topic name directly here for simplicity
    print(f"[Publisher] Published post-created: {event['postId']} | {event['content'][:70]}...")
    return event['postId']

def _publish_post_liked_event(kafka_bootstrap_servers, post_id, user_id):
    prod = _get_producer(kafka_bootstrap_servers)
    event = {
        "postId": post_id,
        "userId": user_id,
        "timestamp": int(time.time() * 1000)
    }
    prod.send("post-liked", value=event) # Topic name directly here for simplicity
    print(f"[Publisher] Published post-liked: User {user_id} liked Post {post_id}")

def publish_mock_events(kafka_bootstrap_servers, dataset_filepath):
    """
    Publishes mock post and like events from the AG News dataset to Kafka.
    This function is designed to be called by the main service entrypoint.
    """
    print(f"\n--- Starting AG News Post and Like Publisher (Integrated) ---")
    
    try:
        df = pd.read_csv(dataset_filepath)
        print(f"[Publisher] Loaded {len(df)} news articles from dataset: {dataset_filepath}")

        df = df.dropna(subset=['Description'])
        df = df[df['Description'].astype(bool)]

        df = df.sample(frac=1, random_state=42).reset_index(drop=True)
        
        published_post_ids = []
        current_post_id = 4000
        current_user_id_for_posts = 100
        active_liking_users = list(range(1, 20))

        posts_published_count = 0
        df_idx = 0

        # --- Stage 1: Publish initial posts for model training ---
        print(f"[Publisher] Stage 1: Publishing initial {INITIAL_POSTS_FOR_TRAINING} posts for model training...")
        while posts_published_count < INITIAL_POSTS_FOR_TRAINING and df_idx < len(df):
            row = df.iloc[df_idx]
            post_content = row['Description']
            
            published_id = _publish_post_created_event(kafka_bootstrap_servers, current_post_id, current_user_id_for_posts, post_content)
            published_post_ids.append(published_id)
            
            current_post_id += 1
            current_user_id_for_posts += 1
            posts_published_count += 1
            df_idx += 1
            time.sleep(DELAY_BETWEEN_EVENTS)
        
        print(f"[Publisher] Stage 1 Complete. Published {posts_published_count} posts.")
        # No need for long sleep here; main.py will handle overall timing.


        # --- Stage 2: Publish remaining posts and interspersed likes ---
        print(f"[Publisher] Stage 2: Publishing remaining posts and interspersing likes...")
        while posts_published_count < TOTAL_POSTS_TO_PUBLISH and df_idx < len(df):
            posts_in_batch = 0
            while posts_in_batch < BATCH_SIZE and posts_published_count < TOTAL_POSTS_TO_PUBLISH and df_idx < len(df):
                row = df.iloc[df_idx]
                post_content = row['Description']
                
                published_id = _publish_post_created_event(kafka_bootstrap_servers, current_post_id, current_user_id_for_posts, post_content)
                published_post_ids.append(published_id)
                
                current_post_id += 1
                current_user_id_for_posts += 1
                posts_published_count += 1
                posts_in_batch += 1
                df_idx += 1
                time.sleep(DELAY_BETWEEN_EVENTS)
            
            print(f"[Publisher] Published {posts_in_batch} new posts. Total: {posts_published_count}")
            
            if published_post_ids:
                print(f"[Publisher] Simulating {LIKES_PER_BATCH} likes...")
                for _ in range(LIKES_PER_BATCH):
                    post_to_like = random.choice(published_post_ids)
                    liking_user_id = random.choice(active_liking_users)
                    _publish_post_liked_event(kafka_bootstrap_servers, post_to_like, liking_user_id)
                    time.sleep(DELAY_BETWEEN_EVENTS * 0.1)
            
            print(f"[Publisher] Waiting before next batch...")
            time.sleep(DELAY_BETWEEN_BATCHES)

        print(f"\n--- [Publisher] Finished publishing {posts_published_count} total posts and associated likes. ---")
        
    except FileNotFoundError:
        print(f"Error: Dataset file not found at {dataset_filepath}. Please ensure it's copied into the Docker image.")
        raise # Re-raise to fail early if dataset isn't found
    except KeyError:
        print(f"Error: Required column 'Description' not found in {dataset_filepath}. Please check dataset format.")
        raise
    except Exception as e:
        print(f"An unexpected error occurred during publishing: {e}", exc_info=True)
        raise
    finally:
        if producer:
            producer.close()
            print("[Publisher] Kafka producer closed.")

# The __main__ block is removed as this script is now imported and called.
# The main service will handle the indefinite running.