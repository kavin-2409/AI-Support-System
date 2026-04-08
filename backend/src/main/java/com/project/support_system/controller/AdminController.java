package com.project.support_system.controller;

import com.project.support_system.entity.Status;
import com.project.support_system.entity.Ticket;
import com.project.support_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private TicketRepository ticketRepository;

    // ✅ Get all tickets
    @GetMapping("/tickets")
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    // ✅ Update ticket status
    @PutMapping("/tickets/{id}")
    public Ticket updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {

        Ticket ticket = ticketRepository.findById(id).orElseThrow();

        String status = body.get("status");
        ticket.setStatus(Status.valueOf(status));

        return ticketRepository.save(ticket);
    }
}