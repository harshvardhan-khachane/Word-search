# Word Search Backend System

A Java/Spring Boot backend system for efficient word management, auto-completion, and usage tracking. Designed to handle large datasets (100k+ words) with fast prefix searches and rank-based suggestions.

## Assumptions

- Words with only lowercase letters [a-z] are considered

## Installation

1. **Prerequisites**:
    - Java 17+
    - Maven 3.6+
    - Node

2. **Clone the Repository and Install Dependencies**:
   ```bash
   git clone https://github.com/harshvardhan-khachane/word-search.git
   cd word-search
   mvn clean install
   
3. **Place your `words.txt` file (one word per line, max 50 characters) inside `src/main/resources/` before starting the server.**
4. **Start backend using `mvn spring-boot:run`**



5. **Start the Frontend**
    - Open a new terminal and navigate to the frontend directory
        ```bash
        cd ./Frontend/vite-project
        npm install
        npm run dev

**The frontend will be available at `http://localhost:5173/`**

# API Documentation

- The backend runs on `localhost:8080`
- You can also import the `Word Search.postman_collection.json` in Postman to test the APIs.

| Method | Endpoint                       | Description                        |
|--------|--------------------------------|------------------------------------|
| POST   | /api/insert?word={word}        | Add new word to system             |
| POST   | /api/search?word={word}        | Search word and increment its rank|
| GET    | /api/auto-complete             | Get suggestions for prefix         |
| GET    | /api/rank?word={word}          | Get current rank of a word         |


## Testing

- **Run all Tests** using `mvn test`

## Features

- **Word Loading**: Bulk load words from text files
- **Auto-Completion**: Instant prefix-based suggestions
- **Usage Tracking**: Real-time rank updates based on word usage
- **Scalable Architecture**: Handles 100k+ words efficiently
- **REST API**: Full CRUD operations via HTTP endpoints

## Performance
- **Prefix Search:** `O(L + M)` Time Complexity
    - L = prefix Length
    - M = number of matches

## Tech Stack

- **Java 24**
- **Spring Boot**
- **Trie Data Structure**: For fast prefix searches
- **ConcurrentHashMap**: Thread-safe rank tracking
- **Maven**: Dependency management
- **JUnit 5**: Testing framework

