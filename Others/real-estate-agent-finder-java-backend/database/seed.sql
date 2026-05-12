USE real_estate_agent_finder;

INSERT INTO users (full_name, email, phone, password_hash, role)
VALUES
    ('System Administrator', 'admin@realestate.com', '+94 77 000 0000', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN'),
    ('Demo Customer', 'customer@realestate.com', '+94 77 111 1111', 'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'CUSTOMER')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name), phone = VALUES(phone), password_hash = VALUES(password_hash), role = VALUES(role);

INSERT INTO agents (full_name, email, phone, city, specialty, experience_years, rating, status, bio)
VALUES
    ('John Silva', 'john.silva@realestate.com', '+94 77 123 1111', 'Colombo', 'Luxury Homes', 8, 4.90, 'AVAILABLE', 'Experienced agent focused on premium urban homes and investor clients.'),
    ('Nimali Perera', 'nimali.perera@realestate.com', '+94 77 123 2222', 'Kandy', 'Apartment Sales', 6, 4.80, 'AVAILABLE', 'Strong apartment sales specialist with excellent local market knowledge.'),
    ('Ashan Fernando', 'ashan.fernando@realestate.com', '+94 77 123 3333', 'Galle', 'Rental Expert', 10, 4.85, 'AVAILABLE', 'Supports rental clients and families with trusted property advice.'),
    ('Sajini De Silva', 'sajini.desilva@realestate.com', '+94 77 123 4444', 'Negombo', 'Commercial Property', 7, 4.70, 'AVAILABLE', 'Helps investors and business owners find commercial spaces.'),
    ('Kasun Perera', 'kasun.perera@realestate.com', '+94 77 123 5555', 'Jaffna', 'Buying Specialist', 5, 4.60, 'AVAILABLE', 'Guides first-time buyers through confident property decisions.'),
    ('Dinithi Fernando', 'dinithi.fernando@realestate.com', '+94 77 123 6666', 'Matara', 'Family Homes', 9, 4.88, 'AVAILABLE', 'Focuses on family homes and long-term property planning.')
ON DUPLICATE KEY UPDATE phone = VALUES(phone), city = VALUES(city), specialty = VALUES(specialty), experience_years = VALUES(experience_years), rating = VALUES(rating), status = VALUES(status), bio = VALUES(bio);

INSERT INTO properties (title, property_type, city, price, bedrooms, bathrooms, property_size, status, agent_id, image_url, description)
VALUES
    ('Luxury Apartment', 'APARTMENT', 'Colombo', 20000000.00, 3, 2, '1800 sq ft', 'AVAILABLE', 1, 'images/luxuryaprtment.jpg', 'Premium city apartment close to business districts and lifestyle amenities.'),
    ('Modern Family House', 'HOUSE', 'Kandy', 30000000.00, 4, 3, '2600 sq ft', 'AVAILABLE', 2, 'images/modernhouse.jpg', 'Spacious family house with parking, garden space and great neighborhood access.'),
    ('Sea View Villa', 'VILLA', 'Galle', 100000000.00, 5, 4, '4200 sq ft', 'AVAILABLE', 3, 'images/seaview.jpg', 'Luxury villa with ocean views, private pool and premium finishings.'),
    ('City Apartment', 'APARTMENT', 'Negombo', 18000000.00, 2, 1, '1200 sq ft', 'AVAILABLE', 4, 'images/property4.jpg', 'Compact city apartment suitable for young professionals or investors.'),
    ('Commercial Office Space', 'COMMERCIAL', 'Colombo', 45000000.00, 0, 2, '3000 sq ft', 'AVAILABLE', 4, 'images/commercial.jpg', 'High-visibility office space ideal for startups or service companies.'),
    ('Investment Land Plot', 'LAND', 'Kurunegala', 12000000.00, 0, 0, '20 perches', 'AVAILABLE', 5, 'images/land.jpg', 'Road-facing land plot suited for future residential development.')
ON DUPLICATE KEY UPDATE city = VALUES(city), price = VALUES(price), bedrooms = VALUES(bedrooms), bathrooms = VALUES(bathrooms), property_size = VALUES(property_size), status = VALUES(status), agent_id = VALUES(agent_id), image_url = VALUES(image_url), description = VALUES(description);
