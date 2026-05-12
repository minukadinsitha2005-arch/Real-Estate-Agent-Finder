package com.realestate.backend.service;

import com.realestate.backend.model.ContactMessage;
import com.realestate.backend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// OOP CONCEPT: POLYMORPHISM - implements CrudService
@Service
public class ContactMessageService implements CrudService<ContactMessage, Long> {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Override
    public List<ContactMessage> getAll() {
        return contactMessageRepository.findAll();
    }

    @Override
    public Optional<ContactMessage> getById(Long id) {
        return contactMessageRepository.findById(id);
    }

    @Override
    public ContactMessage save(ContactMessage contactMessage) {
        return contactMessageRepository.save(contactMessage);
    }

    @Override
    public ContactMessage update(Long id, ContactMessage updatedMessage) {
        ContactMessage existing = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact message not found with id: " + id));

        existing.setFullName(updatedMessage.getFullName());
        existing.setEmail(updatedMessage.getEmail());
        existing.setPhone(updatedMessage.getPhone());
        existing.setCity(updatedMessage.getCity());
        existing.setInquiryType(updatedMessage.getInquiryType());
        existing.setSubject(updatedMessage.getSubject());
        existing.setMessage(updatedMessage.getMessage());

        return contactMessageRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (contactMessageRepository.existsById(id)) {
            contactMessageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
