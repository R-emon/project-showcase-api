# Project Showcase API

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green) ![MongoDB](https://img.shields.io/badge/MongoDB-blueviolet) ![JWT](https://img.shields.io/badge/Auth-JWT-orange)

This repository contains the backend REST API for the DevFolio Project Showcase application. It is a robust, secure, and scalable service built with Spring Boot, designed to handle project data, user authentication, and ownership-based authorization.

The live API is deployed on Render and is consumed by a [Next.js frontend application](https://project-showcase-client.vercel.app).

## ✨ Features

- **Full CRUD Functionality:** Complete Create, Read, Update, and Delete operations for user projects.
- **Secure Authentication:** User registration and login system using stateless JWT (JSON Web Token) authentication.
- **Ownership-Based Authorization:** Users can only update or delete projects they have created.
- **Custom Exception Handling:** Clear and predictable error responses for common issues (e.g., `404 Not Found`, `403 Forbidden`).
- **CORS Configuration:** Properly configured Cross-Origin Resource Sharing to allow requests from the deployed frontend.
- **Containerized Deployment:** Includes a multi-stage `Dockerfile` for efficient and reliable deployment on cloud platforms like Render.

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.x
- **Language:** Java 17
- **Database:** MongoDB (using Spring Data MongoDB)
- **Security:** Spring Security, JSON Web Tokens (JJWT library)
- **Build Tool:** Apache Maven
- **Deployment:** Docker

## 🚀 Getting Started Locally

To get a local copy up and running, follow these simple steps.

### Prerequisites

You will need the following tools installed on your machine:
- Java JDK 17 or later
- Apache Maven
- A running instance of MongoDB (or a free MongoDB Atlas cluster)

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/R-emon/project-showcase-api.git
    cd project-showcase-api
    ```

2.  **Configure Environment Variables:**
    The application requires a configuration file to connect to the database and manage JWTs.
    -   Create a new file named `application.properties` inside the `src/main/resources` directory.
    -   Copy the contents from the example below and fill in your own values.

    **`application.properties` example:**
    ```properties
    # MongoDB Connection String
    # For a local database:
    spring.data.mongodb.uri=mongodb://localhost:27017/project_showcase
    # Or for a MongoDB Atlas cluster:
    # spring.data.mongodb.uri=mongodb+srv://<user>:<pass>@cluster.mongodb.net/project_showcase?retryWrites=true&w=majority

    # JWT Configuration
    # You can generate a strong secret key online
    app.jwt.secret=645267556B58703273357638792F423F4528482B4D6251655468576D5A713474
    # Token expiration time in milliseconds (e.g., 86400000 = 24 hours)
    app.jwt.expiration-ms=86400000
    ```

3.  **Build and Run the Application:**
    -   Use Maven to build the project:
        ```sh
        mvn clean install
        ```
    -   Run the application:
        ```sh
        mvn spring-boot:run
        ```
    The API will now be running on `http://localhost:8080`.

## 📖 API Endpoints

### Public Endpoints (No Authentication Required)

| Method | Endpoint             | Description                      |
| :----- | :------------------- | :------------------------------- |
| `POST` | `/api/auth/register` | Register a new user.             |
| `POST` | `/api/auth/login`    | Log in to get a JWT.             |
| `GET`  | `/api/projects`      | Get a list of all projects.      |
| `GET`  | `/api/projects/{id}` | Get details of a single project. |

### Protected Endpoints (Requires JWT in `Authorization` Header)

| Method   | Endpoint             | Description               |
| :------- | :------------------- | :------------------------ |
| `POST`   | `/api/projects`      | Create a new project.     |
| `PUT`    | `/api/projects/{id}` | Update a project you own. |
| `DELETE` | `/api/projects/{id}` | Delete a project you own. |

## 🚢 Deployment

This application is configured for deployment using Docker. The included `Dockerfile` sets up a multi-stage build that compiles the application and creates a minimal, secure runtime image.

The service is deployed on **Render** and connects to a **MongoDB Atlas** database. The following environment variable is required for deployment:
-   `MONGO_URI`: The full connection string for the MongoDB Atlas database, including the database name.