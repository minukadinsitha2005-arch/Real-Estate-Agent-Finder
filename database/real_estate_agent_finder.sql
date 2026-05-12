-- ================================================================
-- RealtorSL / Real Estate Agent Finder Appointment System
-- Fresh MySQL Database Setup
-- Run this full script in MySQL Workbench before running Spring Boot
-- ================================================================

DROP DATABASE IF EXISTS real_estate_agent_finder;
CREATE DATABASE real_estate_agent_finder;
USE real_estate_agent_finder;

-- Users table: inherited fields from Person + user login fields
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER'
);

-- Agents table: inherited fields from Person + agent details
CREATE TABLE agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    location VARCHAR(255),
    specialization VARCHAR(255),
    experience_years INT,
    image_url VARCHAR(500),
    rating DOUBLE,
    badge VARCHAR(100),
    description TEXT
);

-- Properties table
CREATE TABLE properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    location VARCHAR(255),
    price DOUBLE,
    bedrooms INT,
    bathrooms INT,
    description TEXT,
    image_url VARCHAR(500),
    agent_name VARCHAR(255),
    status VARCHAR(100) DEFAULT 'Available'
);

-- Appointment requests from frontend booking form
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255),
    customer_email VARCHAR(255),
    customer_phone VARCHAR(255),
    city VARCHAR(255),
    request_type VARCHAR(255),
    agent_name VARCHAR(255),
    preferred_date VARCHAR(50),
    preferred_time VARCHAR(50),
    subject VARCHAR(255),
    message VARCHAR(2000),
    status VARCHAR(100) DEFAULT 'Pending'
);

-- Contact messages from contact page
CREATE TABLE contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    city VARCHAR(255),
    inquiry_type VARCHAR(255),
    subject VARCHAR(255),
    message TEXT
);

-- Admin and normal user login accounts
INSERT INTO users (name, email, phone, password, role) VALUES
('Admin User', 'admin@gmail.com', '+94 77 000 0001', 'admin123', 'ADMIN'),
('Normal User', 'user@gmail.com', '+94 77 000 0002', 'user123', 'USER');

-- Sample agents
INSERT INTO agents (name, email, phone, location, specialization, experience_years, image_url, rating, badge, description) VALUES
('John Silva', 'john@realestate.com', '+94 77 111 0001', 'Colombo', 'Luxury Homes', 8, 'images/john.jpg', 5.0, 'Verified', 'Helps clients find premium homes, negotiate better deals, and secure trusted property guidance in Colombo.'),
('Nimali Perera', 'nimali@realestate.com', '+94 77 111 0002', 'Kandy', 'Apartment Sales', 6, 'images/Nimali.jpg', 5.0, 'Top Rated', 'Specializes in apartment sales and buyer guidance with strong local market knowledge in Kandy.'),
('Ashan Fernando', 'ashan@realestate.com', '+94 77 111 0003', 'Galle', 'Rental Expert', 10, 'images/ashan.jpg', 5.0, 'Available', 'Supports families and renters with practical property advice and trusted rental options in southern areas.'),
('Sajini De Silva', 'sajini@realestate.com', '+94 77 111 0004', 'Negombo', 'Commercial Property', 7, 'images/agentspeaking.jpg', 5.0, 'Verified', 'Helps business owners and investors discover commercial spaces and property opportunities with confidence.'),
('Kasun Perera', 'kasun@realestate.com', '+94 77 111 0005', 'Jaffna', 'Buying Specialist', 5, 'images/Findagents.jpg', 5.0, 'Top Rated', 'Guides first-time buyers and families through smooth property decisions with clear communication.');

-- Sample properties
INSERT INTO properties (title, type, location, price, bedrooms, bathrooms, description, image_url, agent_name, status) VALUES
('Luxury Apartment', 'Apartment', 'Colombo', 20000000, 3, 2, 'A modern luxury apartment in the heart of Colombo with stunning city views.', 'images/luxuryaprtment.jpg', 'John Silva', 'Available'),
('Modern Family House', 'House', 'Kandy', 30000000, 4, 3, 'A spacious family home surrounded by greenery in a peaceful Kandy neighbourhood.', 'images/modernhouse.jpg', 'Nimali Perera', 'Available'),
('Sea View Villa', 'Villa', 'Galle', 100000000, 5, 4, 'A breathtaking sea view villa perfect for premium living or investment.', 'images/seaview.jpg', 'Ashan Fernando', 'Available'),
('City Apartment', 'Apartment', 'Negombo', 15000000, 2, 1, 'An affordable city apartment close to beaches and commercial areas in Negombo.', 'images/apartments.jpg', 'Sajini De Silva', 'Available'),
('Commercial Land', 'Land', 'Colombo', 45000000, 0, 0, 'A prime land opportunity ideal for investors and commercial development.', 'images/lands.jpg', 'John Silva', 'Available');

-- Optional sample appointment so admin dashboard has one request at first run
INSERT INTO appointments (customer_name, customer_email, customer_phone, city, request_type, agent_name, preferred_date, preferred_time, subject, message, status) VALUES
('Test Customer', 'customer@gmail.com', '0771234567', 'Colombo', 'Book Appointment', 'John Silva', '2026-05-20', '10:30', 'Viewing Request', 'I would like to meet the agent and view a property.', 'Pending');

-- Optional sample contact message
INSERT INTO contact_messages (full_name, email, phone, city, inquiry_type, subject, message) VALUES
('Demo Visitor', 'visitor@gmail.com', '0712345678', 'Negombo', 'Property Inquiry', 'Need help finding an agent', 'Please contact me about available agents in Negombo.');
