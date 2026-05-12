package com.realestate.backend.service;

import com.realestate.backend.model.Appointment;
import com.realestate.backend.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// OOP CONCEPT: POLYMORPHISM - implements CrudService
@Service
public class AppointmentService implements CrudService<Appointment, Long> {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getStatus() == null || appointment.getStatus().isEmpty()) {
            appointment.setStatus("Pending");
        }
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment update(Long id, Appointment updatedAppointment) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        existing.setCustomerName(updatedAppointment.getCustomerName());
        existing.setCustomerEmail(updatedAppointment.getCustomerEmail());
        existing.setCustomerPhone(updatedAppointment.getCustomerPhone());
        existing.setCity(updatedAppointment.getCity());
        existing.setRequestType(updatedAppointment.getRequestType());
        existing.setAgentName(updatedAppointment.getAgentName());
        existing.setPreferredDate(updatedAppointment.getPreferredDate());
        existing.setPreferredTime(updatedAppointment.getPreferredTime());
        existing.setSubject(updatedAppointment.getSubject());
        existing.setMessage(updatedAppointment.getMessage());
        existing.setStatus(updatedAppointment.getStatus());

        return appointmentRepository.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Update only the status field
    public Appointment updateStatus(Long id, String status) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        existing.setStatus(status);
        return appointmentRepository.save(existing);
    }
}
