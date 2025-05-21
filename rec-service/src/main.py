# recommendation-service/src/main.py

import json
import logging
import threading # Import threading module
import time
from kafka import KafkaConsumer
from config import KAFKA_BOOTSTRAP_SERVERS, POST_CREATED_TOPIC, POST_LIKED_TOPIC, RECOMMENDATION_INTERVAL_SECONDS
from recommender import process_post_created, process_post_liked, generate_recommendations
from mock_event_publisher import publish_mock_events # Import the new function (now in src/)

# Configure logging for the main service
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Dataset path inside the Docker container
AGNEWS_DATASET_PATH = './dataset.csv' # Assuming train.csv is copied to /app

def consume_events():
    """
    Main loop for consuming Kafka events for post creation and likes.
    """
    consumer = None
    try:
        logging.info(f"Connecting to Kafka at {KAFKA_BOOTSTRAP_SERVERS}...")
        consumer = KafkaConsumer(
            POST_CREATED_TOPIC,
            POST_LIKED_TOPIC,
            bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
            group_id='recommendation-service-group', # Use a single group for both topics
            value_deserializer=lambda x: json.loads(x.decode('utf-8')),
            auto_offset_reset='earliest' # Start from beginning if no committed offset
        )
        logging.info("Recommendation service is listening for events...")

        for message in consumer:
            if message.topic == POST_CREATED_TOPIC:
                process_post_created(message.value)
            elif message.topic == POST_LIKED_TOPIC:
                process_post_liked(message.value)

    except Exception as e:
        logging.error(f"Error in Kafka consumer: {e}", exc_info=True)
    finally:
        if consumer:
            consumer.close()
            logging.info("Kafka consumer closed.")

def recommendation_generator_loop():
    """
    Periodically generates and publishes recommendations.
    """
    while True:
        try:
            generate_recommendations()
            time.sleep(RECOMMENDATION_INTERVAL_SECONDS)
        except Exception as e:
            logging.error(f"Error in recommendation generation loop: {e}", exc_info=True)
            time.sleep(5) # Small delay before retrying

def start_publisher_thread(kafka_bootstrap_servers, dataset_filepath):
    """
    Function to run the mock event publisher in a separate thread.
    """
    logging.info("Starting mock event publisher in a separate thread...")
    try:
        publish_mock_events(kafka_bootstrap_servers, dataset_filepath)
        logging.info("Mock event publishing thread finished its task.")
    except Exception as e:
        logging.error(f"Mock event publisher thread encountered an error: {e}", exc_info=True)


if __name__ == '__main__':
    logging.info("Starting recommendation-service...")

    # --- Start publisher in a separate thread ---
    #publisher_thread = threading.Thread(target=start_publisher_thread, args=(KAFKA_BOOTSTRAP_SERVERS, AGNEWS_DATASET_PATH))
    #publisher_thread.daemon = True # Allows the main program to exit even if this thread is still running
    #publisher_thread.start()
    #logging.info("Publisher thread initiated.")
#
    # --- Start recommendation generation in a separate thread ---
    recommendation_thread = threading.Thread(target=recommendation_generator_loop)
    recommendation_thread.daemon = True
    recommendation_thread.start()
    logging.info("Recommendation generation thread initiated.")

    # --- Start consuming Kafka events in the main thread ---
    # This thread will run indefinitely, keeping the process alive.
    consume_events()

    # The main thread will block on consume_events(). When consume_events() exits
    # (e.g., due to an error), the daemon threads will automatically terminate.