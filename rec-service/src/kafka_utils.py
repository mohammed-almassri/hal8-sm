# src/kafka_utils.py

import json
from kafka import KafkaConsumer, KafkaProducer
from config import KAFKA_BOOTSTRAP_SERVERS

def get_kafka_consumer(topic, group_id):
    """Creates and returns a KafkaConsumer instance."""
    return KafkaConsumer(
        topic,
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        auto_offset_reset='earliest', # Start reading from the beginning if no offset is committed
        enable_auto_commit=True,
        group_id=group_id,
        value_deserializer=lambda x: json.loads(x.decode('utf-8')),
        api_version=(0, 10, 2)
    )

def get_kafka_producer():
    """Creates and returns a KafkaProducer instance."""
    return KafkaProducer(
        bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
        value_serializer=lambda x: json.dumps(x).encode('utf-8'),
        api_version=(0, 10, 2)
    )