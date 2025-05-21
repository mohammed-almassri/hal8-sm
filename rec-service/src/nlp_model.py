# recommendation-service/src/nlp_model.py

import logging
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.decomposition import NMF # Non-negative Matrix Factorization for topic modeling
import numpy as np # Used for vector normalization or other numerical ops

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

# Initialize NLP tools
lemmatizer = WordNetLemmatizer()
stop_words = set(stopwords.words('english'))

def preprocess_text(text):
    """
    Basic text preprocessing: lowercase, remove non-alphabetic, tokenize, remove stopwords, lemmatize.
    """
    if not isinstance(text, str):
        return "" # Handle non-string input
    
    text = text.lower()
    # Remove non-alphabetic characters and tokenize
    words = nltk.word_tokenize(text)
    
    # Remove stopwords and lemmatize
    processed_words = [
        lemmatizer.lemmatize(word)
        for word in words
        if word.isalpha() and word not in stop_words
    ]
    return " ".join(processed_words)

def train_topic_model(documents, num_topics):
    """
    Trains a topic model (NMF) on a collection of documents.

    Args:
        documents (list of str): A list of text documents.
        num_topics (int): The number of topics to discover.

    Returns:
        tuple: (nmf_model, feature_names)
            nmf_model (sklearn.decomposition.NMF): The trained NMF model.
            feature_names (list of str): List of feature names (words) from the TF-IDF vectorizer.
    """
    if not documents:
        logging.warning("No documents provided for topic model training.")
        return None, []

    logging.info(f"Training topic model with {len(documents)} documents and {num_topics} topics...")
    
    # Preprocess documents
    processed_documents = [preprocess_text(doc) for doc in documents]
    
    # TF-IDF Vectorization
    # max_df: ignore words that appear in more than X% of documents
    # min_df: ignore words that appear in less than X documents
    vectorizer = TfidfVectorizer(max_df=0.95, min_df=2, stop_words='english')
    tfidf = vectorizer.fit_transform(processed_documents)
    
    # NMF Topic Modeling
    # random_state for reproducibility
    nmf_model = NMF(n_components=num_topics, random_state=1, alpha_W=0.00005, alpha_H=0.00005, l1_ratio=0.5, init='nndsvda')
    nmf_model.fit(tfidf)
    
    feature_names = vectorizer.get_feature_names_out()
    logging.info("Topic model training complete.")
    
    return nmf_model, feature_names

def infer_topic(document, trained_model, feature_names):
    """
    Infers the topic distribution for a single document using a trained NMF model.

    Args:
        document (str): The text document to infer topics for.
        trained_model (sklearn.decomposition.NMF): The trained NMF model.
        feature_names (list of str): Feature names from the vectorizer used for training.

    Returns:
        list of float: The topic distribution vector for the document.
                       Normalized to sum to 1.
    """
    if not document or not trained_model or not feature_names:
        logging.warning("Missing input for topic inference.")
        return [0.0] * trained_model.n_components if trained_model else []

    # Re-create the same vectorizer used during training
    # This is critical to ensure new documents are vectorized consistently
    vectorizer = TfidfVectorizer(max_df=0.95, min_df=2, stop_words='english')
    # Fit with the *same feature names* from training. This makes it a "vocabulary-only" fit.
    # We can't simply fit_transform on a single document, as it would create a new vocabulary.
    # Instead, we need to transform using the vocabulary learned during training.
    
    # A common way to do this is to manually set the vocabulary or ensure the vectorizer object
    # from training is passed along. For simplicity here, we'll re-initialize and set vocabulary.
    # In a real system, you'd save/load the *fitted* vectorizer alongside the NMF model.

    # This is a common workaround for 'transforming' with a pre-existing vocabulary:
    vectorizer.fit_transform([" ".join(feature_names)]) # Dummy fit to create vocabulary
    
    # Transform the single document using the established vocabulary
    processed_document = preprocess_text(document)
    doc_tfidf = vectorizer.transform([processed_document])
    
    # Infer topics
    topic_distribution = trained_model.transform(doc_tfidf)[0]
    
    # Normalize the topic distribution so it sums to 1 (optional, but good practice for distributions)
    topic_sum = np.sum(topic_distribution)
    if topic_sum > 0:
        normalized_topic_distribution = (topic_distribution / topic_sum).tolist()
    else:
        normalized_topic_distribution = [0.0] * trained_model.n_components
        
    return normalized_topic_distribution

# Example of how to get top words for a topic (for debugging/understanding)
def get_top_words_per_topic(model, feature_names, num_top_words=10):
    for topic_idx, topic in enumerate(model.components_):
        top_words = [feature_names[i] for i in topic.argsort()[:-num_top_words - 1:-1]]
        logging.debug(f"Topic {topic_idx}: {' '.join(top_words)}")