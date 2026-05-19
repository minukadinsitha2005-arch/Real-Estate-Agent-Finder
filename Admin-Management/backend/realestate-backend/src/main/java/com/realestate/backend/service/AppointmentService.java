package com.realestate.backend.service;

import com.realestate.backend.model.Appointment;
import com.realestate.backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment createAppointment(Appointment appointment) {
        if (appointment.getStatus() == null || appointment.getStatus().isBlank()) {
            appointment.setStatus("Pending");
        }
        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setCustomerName(appointmentDetails.getCustomerName());
        appointment.setCustomerEmail(appointmentDetails.getCustomerEmail());
        appointment.setCustomerPhone(appointmentDetails.getCustomerPhone());
        appointment.setCity(appointmentDetails.getCity());
        appointment.setRequestType(appointmentDetails.getRequestType());
        appointment.setAgentName(appointmentDetails.getAgentName());
        appointment.setPreferredDate(appointmentDetails.getPreferredDate());
        appointment.setPreferredTime(appointmentDetails.getPreferredTime());
        appointment.setSubject(appointmentDetails.getSubject());
        appointment.setMessage(appointmentDetails.getMessage());
        appointment.setStatus(appointmentDetails.getStatus());

        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}
