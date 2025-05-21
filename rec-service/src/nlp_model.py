# src/nlp_model.py

import logging
import re
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans

# Download NLTK data (run this once)
try:
    nltk.data.find('corpora/stopwords')
    nltk.data.find('corpora/wordnet')
except:
    logging.info("Downloading NLTK stopwords, wordnet, and omw-1.4...")
    nltk.download('stopwords')
    nltk.download('wordnet')
    nltk.download('omw-1.4') # Open Multilingual Wordnet for lemmatizer
    logging.info("NLTK data downloaded.")


lemmatizer = WordNetLemmatizer()
stop_words = set(stopwords.words('english'))

def preprocess_text(text):
    """Basic text preprocessing: lowercase, remove punctuation, tokenize, stopwords, lemmatize."""
    text = text.lower()
    text = re.sub(r'[^\w\s]', '', text) # Remove punctuation
    tokens = text.split()
    tokens = [lemmatizer.lemmatize(word) for word in tokens if word not in stop_words]
    return " ".join(tokens)

class TopicModel:
    """
    A simple topic model using TF-IDF and K-Means clustering.
    Retrains periodically as new posts arrive.
    """
    def __init__(self, num_topics):
        self.vectorizer = TfidfVectorizer(max_features=1000)
        self.kmeans = KMeans(n_clusters=num_topics, random_state=42, n_init=10)
        self.trained = False
        self.topic_labels = {} # cluster_id -> descriptive label (e.g., 'topic_0', 'topic_1')

    def train(self, documents):
        """Trains the topic model on a list of preprocessed documents."""
        if not documents:
            logging.warning("No documents to train the topic model.")
            self.trained = False
            return

        logging.info(f"Training topic model with {len(documents)} documents...")
        try:
            vectorized_documents = self.vectorizer.fit_transform(documents)
            self.kmeans.fit(vectorized_documents)
            self.trained = True
            self.assign_topic_labels()
            logging.info("Topic model trained successfully.")
        except Exception as e:
            logging.error(f"Error training topic model: {e}", exc_info=True)
            self.trained = False

    def predict_topic(self, document):
        """Predicts the topic label for a single preprocessed document."""
        if not self.trained:
            logging.warning("Topic model not trained. Cannot predict topic.")
            return "unknown_topic"
        try:
            vectorized_document = self.vectorizer.transform([document])
            cluster_id = self.kmeans.predict(vectorized_document)[0]
            return self.topic_labels.get(cluster_id, f"topic_{cluster_id}")
        except Exception as e:
            logging.error(f"Error predicting topic for document: {document[:50]}... - {e}", exc_info=True)
            return "prediction_error_topic"

    def assign_topic_labels(self):
        """Generates simple labels for topics based on top words."""
        if not self.trained:
            return

        feature_names = self.vectorizer.get_feature_names_out()
        sorted_centroids = self.kmeans.cluster_centers_.argsort()[:, ::-1]

        self.topic_labels = {}
        for i in range(self.kmeans.n_clusters):
            top_words_indices = sorted_centroids[i, :5] # Get top 5 words
            top_words = [feature_names[idx] for idx in top_words_indices]
            # Use top 3 words as label, replace spaces with underscores for cleaner labels
            self.topic_labels[i] = "_".join(top_words[:3]).replace(" ", "_")
            logging.info(f"Cluster {i} labeled as: '{self.topic_labels[i]}' (Top words: {', '.join(top_words)})")