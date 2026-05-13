# Real Estate Agent Finder Appointment System - Fixed Project

## Project structure

```text
RealEstateAgentFinder_Remade/
├── realestate-agent-finder/                 # Spring Boot backend + static frontend served by localhost:8080
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/realestate/backend/
│       │   ├── controller/
│       │   ├── model/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── config/CorsConfig.java
│       │   ├── DataInitializer.java
│       │   └── RealEstateBackendApplication.java
│       └── resources/
│           ├── application.properties
│           └── static/                      # HTML/CSS/JS frontend used by localhost:8080
├── frontend-source/                         # Same corrected frontend source files
├── database/real_estate_agent_finder.sql    # Complete MySQL database script
└── README.md
```

## How to run

1. Open MySQL Workbench.
2. Open `database/real_estate_agent_finder.sql`.
3. Run the full SQL script.
4. Open `realestate-agent-finder` in IntelliJ IDEA.
5. Open `src/main/resources/application.properties`.
6. Change this line to your own MySQL password:

```properties
spring.datasource.password=root123
```

7. Run `RealEstateBackendApplication.java`.
8. Open the website from Spring Boot:

```text
http://localhost:8080/index.html
```

Do not run the frontend using the `D:/.../frontend-source/index.html` file path when testing backend features. Use localhost.

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

## Fixed features

- Agent cards load from backend.
- Agent Display Profile popup works.
- Agent popup has Book Appointment button.
- Company cards load from backend.
- Company Display Profile popup works.
- Company popup has Book Appointment button.
- Appointment form sends data to backend and MySQL.
- Admin can view appointment requests.
- Property cards load from backend.
- Property Display Property popup works.
- Property Book Viewing scrolls to separate viewing request form.
- Property viewing form sends data to backend and MySQL.
- Admin can view property viewing requests.
- Property categories match the home page card design.
- Category filtering works.
- City filtering works.
- Admin can add and delete agents, companies, and properties.
- Admin-added property category and city are saved and used in frontend filters.
