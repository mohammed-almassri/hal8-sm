# HAL8 – Microservices-Based Social Media Platform

HAL8 is a production-grade social media platform built using a microservices architecture. It enables user interaction through post sharing, real-time chat, following mechanisms, and content recommendations — all designed to scale.

## 🌐 Project Overview

**Main Features:**
- User authentication and authorization
- Real-time chat messaging
- Create, edit, and delete posts
- Follow/unfollow users
- Personalized content recommendations

**Target Users:**
- General users consuming and sharing content
- Content creators engaging with audiences

## 🧱 Microservices Breakdown

### 🔐 Auth Service
- **Purpose**: Manages authentication and authorization
- **Tech Stack**: Java 21+, Spring Boot, PostgreSQL
- **Notes**: Required for initial system access; should be started first

### 💬 Chat Service
- **Purpose**: Enables real-time messaging between users
- **Tech Stack**: Java 21+, Spring Boot, PostgreSQL, WebSockets
- **Notes**: Includes WebSocket support and security configuration

### 📝 Posts Service
- **Purpose**: Manages user-generated content (posts)
- **Tech Stack**: Java 21+, Spring Boot, MongoDB
- **Notes**: Publishes events to Kafka for downstream consumers

### 🔄 Follow Service
- **Purpose**: Handles user follow/unfollow functionality
- **Tech Stack**: Java 21+, Spring Boot, PostgreSQL
- **Notes**: Produces events to Kafka for user relationship updates

### 📊 Recommendation Service (rec-service)
- **Purpose**: Provides content recommendations using NLP and user behavior
- **Tech Stack**: Python 3.8+, Kafka, scikit-learn, pandas, nltk


## 📦 Architecture Overview

- Services communicate via **REST APIs** and **Kafka events**
- `docker-compose.yml` is used for orchestration and containerization
- PostgreSQL and MongoDB used for relational and NoSQL data storage
- Kafka enables asynchronous, decoupled event streaming

```plaintext
 [User] ---> [Auth Service] ---> [Follow / Chat / Posts Services]
                    |
                    +---> [Kafka Bus] <--- [Posts / Follow / Rec Services]
```

## 🐳 Deployment with Docker Compose

To run the system locally:

1. Ensure Docker and Docker Compose are installed
2. From the project root directory:

```bash
docker-compose up --build
```

## 🧪 Development Setup

### Requirements

- Java 21+
- Python 3.8+
- Docker & Docker Compose

## 🛠️ Future Plans

- Add notifications service
- Improve user profile management
- Support for media uploads in posts and chat
- GraphQL API layer for flexible front-end consumption

## 🤝 Contributions

Contributions are welcome! Please open an issue or submit a pull request if you'd like to collaborate.