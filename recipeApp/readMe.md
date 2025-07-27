# 🍽️ Recipe Fetcher API - Spring Boot

A simple Spring Boot REST API that fetches **recipe data** from a public API [`https://dummyjson.com/recipes`](https://dummyjson.com/recipes) and returns it through your own custom endpoint.

---

## 📌 Features

- Fetches recipes from a public API
- Maps JSON data to Java objects
- Returns recipe data via RESTful endpoint `/api/recipes`
- Clean separation using Controller, Service, and Model layers
- Built using Spring Boot and Java 17

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven or compatible build tool
- Internet connection (required to fetch external API)

---

## 🛠️ Installation & Run

Build and run the project

`./mvnw spring-boot:run`

Test the endpoint
Open your browser or Postman:

`http://localhost:8080/api/recipes`