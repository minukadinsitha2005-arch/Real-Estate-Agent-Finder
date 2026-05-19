package com.realestate.backend.controller;

import com.realestate.backend.model.ContactMessage;
import com.realestate.backend.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// All endpoints start with /api/contact-messages
@RestController
@RequestMapping("/api/contact-messages")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    // GET /api/contact-messages
    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(contactMessageService.getAll());
    }

    // GET /api/contact-messages/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessage> getMessageById(@PathVariable Long id) {
        return contactMessageService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/contact-messages
    @PostMapping
    public ResponseEntity<ContactMessage> createMessage(@RequestBody ContactMessage message) {
        ContactMessage saved = contactMessageService.save(message);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // DELETE /api/contact-messages/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        boolean deleted = contactMessageService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("Contact message deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
