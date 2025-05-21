# recommendation-service/Dockerfile

FROM python:3.10-slim-buster

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

RUN python -c "import nltk; nltk.download('stopwords'); nltk.download('wordnet'); nltk.download('omw-1.4')"

# Copy the src directory
COPY src/ src/

# --- Remove these lines if you uncommented out the mock publisher calls in main.py ---
# COPY mock_event_publisher.py .
# COPY train.csv .

CMD ["python", "src/main.py"]