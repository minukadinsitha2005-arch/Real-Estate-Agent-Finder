# Real Estate Agent Finder - Java Backend (SE1020)

This project turns the provided frontend into a Java web application using **JSP/Servlets + MySQL** with additional **file handling logs** to stay closer to the marking guide.

## Why this design
- **Java web technologies**: implemented with Java Servlets and JSP pages.
- **MySQL database**: used as the main persistence layer because the project brief allows file read/write **or** a proper SQL connection.
- **File handling included**: audit and backup logs are written to text files so you can still demonstrate Java file read/write during the viva.
- **Minimum 3 CRUD operations**: the project includes full CRUD for multiple modules.

## Modules included
1. **Authentication**
   - Customer registration
   - Login for customer and admin
   - Logout and session-based admin access control

2. **Agent Management**
   - Create agent
   - View/search agents
   - Update agent details
   - Delete agent

3. **Property Management**
   - Create property
   - View/search properties
   - Update property details
   - Delete property

4. **Service Request Management**
   - Create appointment/support request from the public appointments page
   - View all requests in admin panel
   - Update request status
   - Delete request

5. **Contact Inquiry Management**
   - Create inquiry from the public contact page
   - View all inquiries in admin panel
   - Update inquiry status
   - Delete inquiry

## Technologies
- Java 11
- JSP / Servlets
- MySQL 8+
- HTML / CSS / JavaScript
- Maven WAR project (easy to import into IntelliJ IDEA)

## Import into IntelliJ IDEA
1. Open IntelliJ IDEA.
2. Choose **Open** and select this project folder.
3. Let IntelliJ import the Maven project.
4. Configure Apache Tomcat 9 or 10 (Tomcat 9 is recommended because the project uses `javax.servlet`).
5. Deploy the generated WAR artifact.

## Database setup
1. Create the database and tables:
   - Run `database/schema.sql`
2. Insert sample data:
   - Run `database/seed.sql`
3. Update DB credentials in:
   - `src/main/resources/db.properties`

## Default login
- **Admin**: `admin@realestate.com` / `admin123`
- **Customer**: `customer@realestate.com` / `user123`

## Important URLs
- Public home page: `/index.html`
- Authentication: `/auth`
- Admin dashboard: `/admin/dashboard`
- Admin agents: `/admin/agents`
- Admin properties: `/admin/properties`
- Admin service requests: `/admin/requests`
- Admin inquiries: `/admin/inquiries`
- JSON API agents: `/api/agents`
- JSON API properties: `/api/properties`

## File handling used for marking
Even though MySQL is the main storage layer, the following text files are also maintained inside `WEB-INF/data/` when the app runs:
- `admin-audit-log.txt`
- `service-requests.txt`
- `contact-inquiries.txt`

This helps you demonstrate file handling during the viva and aligns with the marking sheet.

## Notes about the provided frontend
- The original public pages were copied into `src/main/webapp/`.
- The **Appointments** and **Contact** forms were connected to real servlets.
- Login buttons now redirect to `/auth` instead of relying on the incomplete modal.
- Image files were not part of the upload, so place your own images inside `src/main/webapp/images/`.

## Suggested viva talking points
- DAO pattern and separation of concerns
- OOP concepts used in the model layer
- How session-based admin protection works
- Why MySQL was chosen and how file logs were added for backup/audit
- Mapping between frontend pages and backend servlets
