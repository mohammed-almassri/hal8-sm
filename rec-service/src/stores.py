# src/stores.py

from collections import defaultdict

# In-memory Stores (for simplicity)
# In a real application, replace with a persistent store (e.g., Redis, database)

# postId -> {postId, userId, content, timestamp, topic_label}
posts_store = {}

# userId -> {topic_label1, topic_label2, ...}
user_interests = defaultdict(set)

# Store preprocessed content for re-training the topic model
post_contents = []

# Map post_id to its index in post_contents for quick retrieval
post_id_to_content_idx = {}