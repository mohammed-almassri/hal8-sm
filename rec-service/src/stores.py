# recommendation-service/src/stores.py

import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# --- In-memory data stores ---
# These are global dictionaries that will hold the state of your system.
# In a production system, these would be backed by a persistent database (e.g., Redis, PostgreSQL).

# Stores post details: postId -> {'userId': UUID, 'content': str}
user_post_store = {}
logging.info("Initialized user_post_store.")

# Stores the inferred topic vector for each post: postId -> list[float] (topic vector)
post_topic_store = {}
logging.info("Initialized post_topic_store.")

# Stores the NLP topic model and its feature names:
# {'model': trained_model_object, 'feature_names': list[str]}
topic_model_store = {}
logging.info("Initialized topic_model_store.")

# Stores the accumulated interest vector for each user: userId (UUID) -> list[float] (user interest vector)
user_vector_store = {}
logging.info("Initialized user_vector_store.")

# You can add functions here later if you want to encapsulate access
# e.g., def get_user_vector(user_id): return user_vector_store.get(user_id)
# But for simple access, directly using the dictionaries is fine for now.