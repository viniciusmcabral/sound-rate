# SoundRate API

A RESTful API for the SoundRate application, a web site for discovering, rating, and reviewing music. This project was created for portfolio purposes.

## ✨ Features

- **User Authentication:** Secure user registration, login, and JWT-based authentication.
- **Account Management:** Profile, avatar, and password updates. Full "Forgot Password" flow with email integration.
- **Ratings & Reviews:** A comprehensive rating system for albums and tracks (0.5 to 5 stars), with the ability to write detailed reviews.
- **Social Features:** Ability to follow other users and like albums and reviews.
- **Dashboards:** Endpoints for data aggregation, such as the highest-rated albums and most active users.
- **External API Integration:** Fetches album, artist, and track data from the official Deezer API.
- **Documentation:** Fully documented API using Swagger/OpenAPI.

## 🛠️ Tech Stack

- **Backend:** Java 17, Spring Boot 3
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Database:** Spring Data JPA, Hibernate, PostgreSQL
- **Email:** Spring Mail + SendGrid
- **Documentation:** Springdoc OpenAPI (Swagger)
- **Containerization:** Docker & Docker Compose

## 🚀 Getting Started

### Prerequisites
- Java JDK 17 or higher
- Maven 3.8+
- Docker and Docker Compose

### Installation Steps

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/viniciusmcabral/sound-rate.git
    cd sound-rate
    ```

2.  **Create the environment file (`.env`):**
    In the project's root directory, create a file named `.env`. It must contain all the necessary environment variables. Use the example below as a template.
    
    **.env.example**
    ```env
    # Variables for the Postgres container
    POSTGRES_DB=sound_rate_db
    POSTGRES_USER=sound_rate_user
    POSTGRES_PASSWORD=your_password

    # Secret for JWT generation
    JWT_SECRET=your_secret_jwt_key

    # Connection URL for Cloudinary image uploads
    CLOUDINARY_URL=cloudinary://...

    # API Key for SendGrid email delivery
    SENDGRID_API_KEY=SG.your_sendgrid_api_key.xxxxxxxx
    ```

3.  **Run with Docker Compose:**
    This command will build the backend and database images and start the containers.
    ```bash
    docker-compose up --build -d
    ```
    The API will be available at `http://localhost:8080/api/v1`.

## 📖 API Documentation (Swagger)

The complete and interactive API documentation is available via the Swagger UI. With the application running, access the following URL in your browser:

[**http://localhost:8080/api/v1/swagger-ui/index.html**](http://localhost:8080/api/v1/swagger-ui/index.html)

## 🗺️ API Endpoints Overview

The API is organized around the following main resources:

- `/auth`: Registration, login, and password management.
- `/users`: User profiles, followers, and self-account management.
- `/albums`: Album details and data aggregations.
- `/artists`: Artist details.
- `/ratings`: Creating and deleting ratings.
- `/reviews`: Creating, updating, and deleting reviews.
- `/search`: Global search across the platform.

For a detailed description of each route, please refer to the `documentation.txt` file or the Swagger documentation.