package com.realestate.backend;

import com.realestate.backend.model.Agent;
import com.realestate.backend.model.Property;
import com.realestate.backend.model.User;
import com.realestate.backend.repository.AgentRepository;
import com.realestate.backend.repository.PropertyRepository;
import com.realestate.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// -------------------------------------------------------
// DataInitializer runs once when the application starts.
// It inserts sample data into the database if tables are empty.
// -------------------------------------------------------
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        // -------------------------------------------------------
        // Insert sample agents (only if the table is empty)
        // -------------------------------------------------------
        if (agentRepository.count() == 0) {

            agentRepository.save(new Agent(
                    "John Silva", "john@realestate.com", "+94 77 111 0001",
                    "Colombo", "Luxury Homes", 8,
                    "images/agent1.jpg", 5.0, "Verified",
                    "Helps clients find premium homes, negotiate better deals, and secure trusted property guidance in Colombo."
            ));

            agentRepository.save(new Agent(
                    "Nimali Perera", "nimali@realestate.com", "+94 77 111 0002",
                    "Kandy", "Apartment Sales", 6,
                    "images/agent2.jpg", 5.0, "Top Rated",
                    "Specializes in apartment sales and buyer guidance with strong local market knowledge in Kandy."
            ));

            agentRepository.save(new Agent(
                    "Ashan Fernando", "ashan@realestate.com", "+94 77 111 0003",
                    "Galle", "Rental Expert", 10,
                    "images/agent3.jpg", 5.0, "Available",
                    "Supports families and renters with practical property advice and trusted rental options in southern areas."
            ));

            agentRepository.save(new Agent(
                    "Sajini De Silva", "sajini@realestate.com", "+94 77 111 0004",
                    "Negombo", "Commercial Property", 7,
                    "images/agent4.jpg", 5.0, "Verified",
                    "Helps business owners and investors discover commercial spaces and property opportunities with confidence."
            ));

            agentRepository.save(new Agent(
                    "Kasun Perera", "kasun@realestate.com", "+94 77 111 0005",
                    "Jaffna", "Buying Specialist", 5,
                    "images/agent5.jpg", 5.0, "Top Rated",
                    "Guides first-time buyers and families through smooth property decisions with clear communication."
            ));

            System.out.println("✅ Sample agents inserted.");
        }

        // -------------------------------------------------------
        // Insert sample properties (only if the table is empty)
        // -------------------------------------------------------
        if (propertyRepository.count() == 0) {

            propertyRepository.save(new Property(
                    "Luxury Apartment", "Apartment", "Colombo",
                    20000000, 3, 2,
                    "A modern luxury apartment in the heart of Colombo with stunning city views.",
                    "images/luxuryaprtment.jpg", "John Silva", "Available"
            ));

            propertyRepository.save(new Property(
                    "Modern Family House", "House", "Kandy",
                    30000000, 4, 3,
                    "A spacious family home surrounded by greenery in a peaceful Kandy neighbourhood.",
                    "images/modernhouse.jpg", "Nimali Perera", "Available"
            ));

            propertyRepository.save(new Property(
                    "Sea View Villa", "Villa", "Galle",
                    100000000, 5, 4,
                    "A breathtaking sea view villa perfect for premium living or investment.",
                    "images/seaview.jpg", "Ashan Fernando", "Available"
            ));

            propertyRepository.save(new Property(
                    "City Apartment", "Apartment", "Negombo",
                    15000000, 2, 1,
                    "An affordable city apartment close to beaches and commercial areas in Negombo.",
                    "images/property4.jpg", "Sajini De Silva", "Available"
            ));

            propertyRepository.save(new Property(
                    "Commercial Office Space", "Commercial", "Colombo",
                    45000000, 0, 2,
                    "A prime commercial office space ideal for startups and growing businesses in Colombo.",
                    "images/company1.jpg", "John Silva", "Available"
            ));

            System.out.println("✅ Sample properties inserted.");
        }

        // -------------------------------------------------------
        // Insert sample users (only if the table is empty)
        // -------------------------------------------------------
        if (userRepository.count() == 0) {

            // Admin user
            User admin = new User(
                    "Admin User", "admin@gmail.com", "+94 77 000 0001",
                    "admin123", "ADMIN"
                    // NOTE: In a real system, use BCrypt to hash passwords
            );
            userRepository.save(admin);

            // Normal user
            User user = new User(
                    "Normal User", "user@gmail.com", "+94 77 000 0002",
                    "user123", "USER"
            );
            userRepository.save(user);

            System.out.println("✅ Sample users inserted.");
        }

        System.out.println("🚀 RealtorSL Backend is running at http://localhost:8080");
    }
}
