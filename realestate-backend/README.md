# RealtorSL – Real Estate Agent Finder
## SE1020 Object Oriented Programming – Backend Setup Guide

---

## 📁 Project Structure

```
real-estate-backend/
├── pom.xml
├── src/main/
│   ├── java/com/realestate/backend/
│   │   ├── RealEstateBackendApplication.java
│   │   ├── DataInitializer.java
│   │   ├── config/
│   │   │   └── CorsConfig.java
│   │   ├── controller/
│   │   │   ├── AgentController.java
│   │   │   ├── PropertyController.java
│   │   │   ├── AppointmentController.java
│   │   │   ├── ContactMessageController.java
│   │   │   └── UserController.java
│   │   ├── model/
│   │   │   ├── Person.java          ← Parent class (Inheritance)
│   │   │   ├── Agent.java           ← extends Person
│   │   │   ├── User.java            ← extends Person
│   │   │   ├── Property.java
│   │   │   ├── Appointment.java
│   │   │   └── ContactMessage.java
│   │   ├── repository/
│   │   │   ├── AgentRepository.java
│   │   │   ├── PropertyRepository.java
│   │   │   ├── AppointmentRepository.java
│   │   │   ├── ContactMessageRepository.java
│   │   │   └── UserRepository.java
│   │   └── service/
│   │       ├── CrudService.java     ← Interface (Polymorphism)
│   │       ├── AgentService.java
│   │       ├── PropertyService.java
│   │       ├── AppointmentService.java
│   │       ├── ContactMessageService.java
│   │       └── UserService.java
│   └── resources/
│       └── application.properties
```

---

## 🛠️ Step 1 – MySQL Database Setup

1. Open **MySQL Workbench** or **phpMyAdmin**
2. Run this SQL (the app also creates it automatically):
   ```sql
   CREATE DATABASE IF NOT EXISTS real_estate_agent_finder;
   ```
3. Default credentials: `username: root` / `password: root`
4. If your password is different, update it in:
   ```
   src/main/resources/application.properties
   ```
   Change this line:
   ```
   spring.datasource.password=root
   ```

---

## 🚀 Step 2 – Run in IntelliJ IDEA

1. Open IntelliJ IDEA
2. Click **File → Open** → select the `real-estate-backend` folder
3. Wait for Maven to download dependencies (bottom progress bar)
4. Open `RealEstateBackendApplication.java`
5. Click the **▶ Run** button (green play icon) at the top
6. Watch the console – you should see:
   ```
   ✅ Sample agents inserted.
   ✅ Sample properties inserted.
   ✅ Sample users inserted.
   🚀 RealtorSL Backend is running at http://localhost:8080
   ```

---

## 🌐 Step 3 – Connect Frontend

**Replace your old `script.js`** with the new `script.js` included in this package.

Open your HTML files in a browser:
- Option A: Double-click `index.html` (file://)
- Option B: Use VS Code **Live Server** extension

The frontend connects to backend at: `http://localhost:8080/api`

---

## 🧪 Step 4 – Test APIs

Open your browser and try these URLs:

| What | URL |
|------|-----|
| All agents | http://localhost:8080/api/agents |
| Search by city | http://localhost:8080/api/agents?location=Colombo |
| All properties | http://localhost:8080/api/properties |
| Search by type | http://localhost:8080/api/properties?type=Apartment |
| All appointments | http://localhost:8080/api/appointments |
| All messages | http://localhost:8080/api/contact-messages |
| All users | http://localhost:8080/api/users |

### Testing with Postman

**Register user:**
- POST `http://localhost:8080/api/users/register`
- Body (JSON): `{ "name": "Test", "email": "test@test.com", "phone": "0771234567", "password": "pass123" }`

**Login:**
- POST `http://localhost:8080/api/users/login`
- Body (JSON): `{ "email": "admin@gmail.com", "password": "admin123" }`

**Create appointment:**
- POST `http://localhost:8080/api/appointments`
- Body (JSON):
```json
{
  "customerName": "Kamal Perera",
  "customerEmail": "kamal@test.com",
  "customerPhone": "0771234567",
  "city": "Colombo",
  "requestType": "Book Appointment",
  "agentName": "John Silva",
  "preferredDate": "2026-06-15",
  "preferredTime": "10:00",
  "subject": "Property viewing",
  "message": "I would like to view the luxury apartment."
}
```

---

## 🎓 OOP Concepts Used

### 1. Encapsulation
- All model fields are `private`
- All fields have `public` getters and setters
- Internal data is hidden from outside access

### 2. Inheritance
- `Person` is the parent class (`@MappedSuperclass`)
- `Agent extends Person` → inherits id, name, email, phone
- `User extends Person` → inherits id, name, email, phone
- Avoids duplicate code

### 3. Polymorphism
- `CrudService<T, ID>` is a generic interface
- Five service classes all implement the same interface
- Each provides its own version of `getAll()`, `save()`, `update()`, `delete()`
- Same method name, different behavior per class

---

## 💾 Default Login Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@gmail.com | admin123 |
| User | user@gmail.com | user123 |

---

## 📋 API Endpoints Summary

### Agents
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | /api/agents | Get all agents |
| GET | /api/agents/{id} | Get agent by ID |
| GET | /api/agents?location=Colombo | Search by location |
| GET | /api/agents?specialization=Luxury | Search by specialty |
| POST | /api/agents | Create agent |
| PUT | /api/agents/{id} | Update agent |
| DELETE | /api/agents/{id} | Delete agent |

### Properties
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | /api/properties | Get all |
| GET | /api/properties?location=Colombo | By location |
| GET | /api/properties?type=Apartment | By type |
| GET | /api/properties?keyword=house | By keyword |
| POST | /api/properties | Create |
| PUT | /api/properties/{id} | Update |
| DELETE | /api/properties/{id} | Delete |

### Appointments
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | /api/appointments | Get all |
| GET | /api/appointments/{id} | Get by ID |
| POST | /api/appointments | Create (from form) |
| PUT | /api/appointments/{id} | Update |
| PUT | /api/appointments/{id}/status | Update status only |
| DELETE | /api/appointments/{id} | Delete |

### Contact Messages
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | /api/contact-messages | Get all |
| GET | /api/contact-messages/{id} | Get by ID |
| POST | /api/contact-messages | Create (from form) |
| DELETE | /api/contact-messages/{id} | Delete |

### Users
| Method | Endpoint | Action |
|--------|----------|--------|
| GET | /api/users | Get all |
| GET | /api/users/{id} | Get by ID |
| GET | /api/users/search?email=x | Find by email |
| POST | /api/users/register | Register |
| POST | /api/users/login | Login |
| PUT | /api/users/{id} | Update |
| DELETE | /api/users/{id} | Delete |

---

## 📦 GitHub Commit Suggestions

```
git init
git add .
git commit -m "Initial project setup - Spring Boot backend"

git add src/main/java/com/realestate/backend/model/
git commit -m "Add model classes: Person, Agent, User, Property, Appointment, ContactMessage"

git add src/main/java/com/realestate/backend/repository/
git commit -m "Add JPA repositories for all entities"

git add src/main/java/com/realestate/backend/service/
git commit -m "Add service layer with CrudService interface and implementations"

git add src/main/java/com/realestate/backend/controller/
git commit -m "Add REST controllers for all API endpoints"

git add src/main/java/com/realestate/backend/DataInitializer.java
git commit -m "Add DataInitializer with sample agents, properties, and users"

git add script.js
git commit -m "Update frontend script.js to connect with Spring Boot backend API"
```

---

## ❓ Troubleshooting

**Port already in use:**
- Stop any other application using port 8080
- Or change `server.port=8080` to `server.port=8081` in application.properties

**MySQL connection failed:**
- Make sure MySQL is running
- Check username/password in application.properties
- Make sure MySQL is on port 3306

**CORS error in browser:**
- Make sure backend is running before opening HTML files
- CorsConfig.java allows all origins including file://

**Frontend shows "Failed to load":**
- Backend must be running at localhost:8080
- Check browser console (F12) for specific errors
