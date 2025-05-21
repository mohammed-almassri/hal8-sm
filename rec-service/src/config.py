# src/config.py
import os

KAFKA_BOOTSTRAP_SERVERS = os.getenv('KAFKA_BOOTSTRAP_SERVERS', 'localhost:29092')
POST_CREATED_TOPIC = 'post-created'
POST_LIKED_TOPIC = 'post-liked'
REC_GENERATED_TOPIC = 'rec-generated'
RECOMMENDATION_INTERVAL_SECONDS = 60
TOPIC_MODEL_UPDATE_INTERVAL_POSTS = 10 # Re-train topic model after this many new posts
NUM_TOPICS = 5 # Number of topics for K-Means clustering