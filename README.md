# SoundRate - Full Stack Music Rating Application

SoundRate is a full-stack web application for discovering, rating, and reviewing music. It was created as a portfolio project.

This repository contains two main projects:
- **`/backend`**: A RESTful API built with Spring Boot.
- **`/frontend`**: A single-page application built with Angular.

---

## ✨ Features

- **Full User Authentication:** Secure registration, login, and JWT-based sessions.
- **Complete Account Management:** Users can update their profile, password, and avatar.
- **Forgot/Reset Password Flow:** A secure, token-based process with email notifications via SendGrid.
- **Ratings & Reviews:** Users can rate albums and tracks (0.5-5 stars) and write detailed reviews.
- **Social Interaction:** Follow other users and like albums or reviews.
- **Dynamic Homepage:** Features dashboards for the highest-rated albums and most active users.
- **Global Search:** Reactive search functionality to find albums, artists, and users.
- **External API Integration:** Music data is fetched and integrated from the official Deezer API.
- **Containerized Environment:** The entire application (frontend, backend, database) is containerized with Docker for easy setup and deployment.

## 🛠️ Tech Stack

### Backend
- **Framework:** Java 17, Spring Boot 3
- **Database:** PostgreSQL with Spring Data JPA / Hibernate
- **Security:** Spring Security & JWT
- **Email:** SendGrid
- **Documentation:** Springdoc OpenAPI (Swagger)

### Frontend
- **Framework:** Angular
- **Language:** TypeScript
- **Styling:** SCSS & Angular Material
- **Reactivity:** RxJS

### Infrastructure
- **Containerization:** Docker & Docker Compose
- **Web Server:** Nginx (for the frontend)

## 🚀 Getting Started

This project is designed to be run easily using Docker.

### Prerequisites
- Docker & Docker Compose

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/viniciusmcabral/sound-rate.git
    cd sound-rate
    ```

2.  **Create the Environment File (`.env`):**
    In the project's root directory, create a file named `.env`. This file will provide all the necessary secrets and configurations to the Docker containers. Use the `.env.example` file (located in the backend README) as a template.
    
    **Example `.env` content:**
    ```env
    # Variables for the Postgres container
    POSTGRES_DB=sound_rate_db
    POSTG-RES_USER=sound_rate_user
    POSTGRES_PASSWORD=your_password

    # Secret for JWT generation
    JWT_SECRET=your_secret_jwt_key

    # API Key for SendGrid email delivery
    SENDGRID_API_KEY=SG.your_sendgrid_api_key.xxxxxxxx
    ```

3.  **Build and Run with Docker Compose:**
    This single command will build the images for the frontend and backend, start all containers, and connect them.
    ```bash
    docker-compose up --build -d
    ```

4.  **Access the Application:**
    - **Frontend:** [http://localhost:4200](http://localhost:4200)
    - **Backend API:** [http://localhost:8080/api/v1](http://localhost:8080/api/v1)
    - **API Documentation (Swagger):** [http://localhost:8080/api/v1/swagger-ui/index.html](http://localhost:8080/api/v1/swagger-ui/index.html)

## 📂 Project Structure

The repository is organized as a monorepo with two main projects:

- **`/backend`**: Contains the Spring Boot application, including all controllers, services, repositories, and models for the API. See the [backend/README.md](backend/README.md) for more details.
- **`/frontend`**: Contains the Angular application, including all pages, components, and services for the user interface. See the [frontend/README.md](frontend/README.md) for more details.