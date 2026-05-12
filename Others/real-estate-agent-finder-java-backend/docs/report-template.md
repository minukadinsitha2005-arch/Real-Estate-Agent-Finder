# Final Report Template

## 1. Project Title
Real Estate Agent Finder - Java Servlet and MySQL Backend

## 2. Student Contribution
- Backend Java architecture
- MySQL database design
- CRUD modules for Agents, Properties, Service Requests, and Contact Inquiries
- File handling logs for audit and backup support

## 3. OOP Concepts Used
- Encapsulation: private fields + getters/setters in model classes
- Inheritance: Person -> Agent / AbstractUser, AbstractUser -> AdminUser / Customer
- Polymorphism: overridden methods in Property subclasses and User subclasses
- Abstraction: Person, AbstractUser, Property, AdminBaseServlet, CrudRepository interface
- Information hiding: DAO + Service layers hide JDBC details from UI and Servlet layer

## 4. CRUD Operations Demonstrated
- Agent Management: Create, Read, Update, Delete
- Property Management: Create, Read, Update, Delete
- Service Request Management: Create, Read, Update, Delete
- Contact Inquiry Management: Create, Read, Update, Delete

## 5. File Handling Included
- Text backups for contact inquiries and service requests
- Admin audit log written to WEB-INF/data/admin-audit-log.txt
- Recent file logs displayed in the admin dashboard

## 6. GitHub Commit History
Add screenshots or exported commit list here.

## 7. Screenshots / UI Evidence
- Public pages
- Auth page
- Admin dashboard
- Agent management
- Property management
- Service request management
- Inquiry management

## 8. Viva Preparation Notes
Explain DAO layer, service layer, servlet flow, session handling, password hashing, and MySQL schema.
