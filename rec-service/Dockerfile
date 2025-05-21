# recommendation-service/Dockerfile

FROM python:3.10-slim-buster

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

RUN python -c "import nltk; nltk.download('stopwords'); nltk.download('wordnet'); nltk.download('omw-1.4')"

# Copy the entire src directory, which now includes mock_event_publisher.py
COPY src/ src/

# Copy the dataset into the /app directory (assuming it's in the root of recommendation-service/)
COPY dataset.csv .

CMD ["python", "src/main.py"]