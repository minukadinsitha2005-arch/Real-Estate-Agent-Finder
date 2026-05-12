# Real Estate Agent Finder Appointment System - Remade Project

This project was remade using the same clean structure style as the bakery Spring Boot project, but the frontend remains normal HTML/CSS/JavaScript. No JSP conversion was done.

## Project structure

```text
RealEstateAgentFinder_Remade/
├── realestate-agent-finder/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/realestate/backend/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── DataInitializer.java
│       │   └── RealEstateBackendApplication.java
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── index.html
│               ├── agents.html
│               ├── appointments.html
│               ├── admin.html
│               ├── properties.html
│               ├── contact.html
│               ├── about.html
│               ├── css/style.css
│               ├── js/script.js
│               └── images/
├── frontend-source/
└── database/real_estate_agent_finder.sql
```

## How to run

1. Open MySQL Workbench.
2. Open `database/real_estate_agent_finder.sql`.
3. Run the full SQL script.
4. Open `realestate-agent-finder` in IntelliJ IDEA.
5. Open `src/main/resources/application.properties`.
6. Change `spring.datasource.password=root123` to your MySQL password.
7. Run `RealEstateBackendApplication.java`.
8. Open this URL in browser:

```text
http://localhost:8080/index.html
```

## Login details

Admin:

```text
Email: admin@gmail.com
Password: admin123
```

Normal user:

```text
Email: user@gmail.com
Password: user123
```

## API endpoints

```text
GET    /api/agents
POST   /api/agents
PUT    /api/agents/{id}
DELETE /api/agents/{id}

GET    /api/properties
POST   /api/properties
PUT    /api/properties/{id}
DELETE /api/properties/{id}

GET    /api/appointments
POST   /api/appointments
PUT    /api/appointments/{id}
PUT    /api/appointments/{id}/status
DELETE /api/appointments/{id}

GET    /api/contact-messages
POST   /api/contact-messages
DELETE /api/contact-messages/{id}

POST   /api/users/register
POST   /api/users/login
```

## OOP concepts included

- Encapsulation: private fields with getters/setters in model classes.
- Inheritance: `Agent` and `User` extend `Person`.
- Polymorphism: service classes implement the generic `CrudService<T, ID>` interface.
- CRUD: Create, Read, Update, Delete are implemented for agents, properties, users, appointments, and contact messages.
