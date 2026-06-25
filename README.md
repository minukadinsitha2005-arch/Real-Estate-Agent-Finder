# 🏠 Real Estate Agent Finder

A full-stack web application that helps users find real estate agents, explore available properties, and book appointments online.

This project was developed as a Java Spring Boot web application with a modern frontend interface and MySQL database integration.

---

## 📌 Project Overview

The **Real Estate Agent Finder** platform allows users to search for real estate agents and properties in Sri Lanka. Users can view agent details, explore properties by city or category, send messages, and request appointments.

An admin dashboard is included to manage users, properties, agents, appointments, and customer messages.

---

## ✨ Main Features

### 👤 User Features

* Browse real estate agents and companies
* View agent details
* Book appointments with agents
* Explore properties by category and city
* View featured properties
* Send contact messages
* User registration and login
* View appointment requests through a user dashboard

### 🛠️ Admin Features

* Admin login dashboard
* Manage users
* Manage properties
* Add, edit, and delete property details
* Manage cities and property categories
* View customer booking requests
* View contact messages
* Manage agent and company information

---

## 🧰 Technologies Used

### Backend

* Java
* Spring Boot
* Spring Data JPA
* REST API
* Maven
* MySQL

### Frontend

* HTML
* CSS
* JavaScript

### Tools

* IntelliJ IDEA
* Visual Studio Code
* MySQL Workbench
* Git and GitHub
* Postman

---

## 📂 Project Structure

```text
realestate-agent-finder/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com.realestate.backend/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── model/
│       │       ├── repository/
│       │       ├── service/
│       │       └── RealEstateBackendApplication.java
│       │
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   ├── images/
│           │   ├── js/
│           │   ├── index.html
│           │   ├── agents.html
│           │   ├── properties.html
│           │   ├── appointments.html
│           │   ├── contact.html
│           │   └── admin.html
│           │
│           └── application.properties
│
├── database/
├── pom.xml
└── README.md
```

---

## ⚙️ How to Run the Project

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/Real-Estate-Agent-Finder.git
```

### 2. Open the project

Open the project using IntelliJ IDEA or VS Code.

### 3. Create the MySQL database

Create a database in MySQL:

```sql
CREATE DATABASE real_estate_db;
```

### 4. Update database settings

Open:

```text
src/main/resources/application.properties
```

Update your MySQL username and password:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/real_estate_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 5. Run the backend

Run this file:

```text
RealEstateBackendApplication.java
```

The backend will start at:

```text
http://localhost:8080
```

---

## 🔗 API Base URL

```text
http://localhost:8080/api
```

Example API endpoints:

```text
GET    /api/agents
POST   /api/appointments
GET    /api/properties
GET    /api/cities
```

---

## 👨‍💻 Author

**Minuka Dinsitha**
IT Undergraduate | SLIIT
Interested in Full-Stack Development and Data Science

---

## 📜 License

This project was created for educational and portfolio purposes.
