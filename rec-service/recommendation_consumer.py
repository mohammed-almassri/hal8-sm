# recommendation-service/recommendation_consumer.py

import json
import logging
from kafka import KafkaConsumer
from src.config import KAFKA_BOOTSTRAP_SERVERS, REC_GENERATED_TOPIC

# Configure logging for this consumer
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

def consume_recommendations():
    """
    Consumes messages from the rec.generated topic and prints them.
    """
    consumer = None
    try:
        logging.info(f"Connecting to Kafka at {KAFKA_BOOTSTRAP_SERVERS} to consume from topic: {REC_GENERATED_TOPIC}")
        consumer = KafkaConsumer(
            REC_GENERATED_TOPIC,
            bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
            auto_offset_reset='earliest',  # Start reading from the beginning if no offset is committed
            enable_auto_commit=True,       # Automatically commit offsets
            group_id='recommendation-viewer-group', # A unique group ID for this consumer
            value_deserializer=lambda x: json.loads(x.decode('utf-8'))
        )
        logging.info(f"Successfully connected and listening for recommendations on '{REC_GENERATED_TOPIC}'...")

        for message in consumer:
            recommendation_data = message.value
            logging.info(f"--- RECEIVED RECOMMENDATION ---")
            logging.info(f"  Partition: {message.partition}, Offset: {message.offset}")
            logging.info(f"  Post ID: {recommendation_data.get('postId')}")
            logging.info(f"  Recommended to Users: {recommendation_data.get('users')}")
            logging.info(f"-----------------------------")

    except Exception as e:
        logging.error(f"Error consuming recommendations: {e}", exc_info=True)
    finally:
        if consumer:
            consumer.close()
            logging.info("Kafka consumer closed.")

if __name__ == '__main__':
    consume_recommendations()