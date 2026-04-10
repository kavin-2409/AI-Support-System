package com.project.support_system.controller;

import com.project.support_system.entity.Message;
import com.project.support_system.entity.Ticket;
import com.project.support_system.entity.Status;
import com.project.support_system.repository.MessageRepository;
import com.project.support_system.repository.TicketRepository;
import com.project.support_system.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TicketRepository ticketRepository;

    // ✅ CREATE TICKET (FIXED)
    @PostMapping
    public Map<String, Object> createTicket(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request
    ) {
        String token = authHeader.replace("Bearer ", "");
        return ticketService.createTicket(request.get("query"), token);
    }

    // ✅ ADD MESSAGE (FIXED)
    @PostMapping("/{ticketId}/messages")
    public Map<String, Object> addMessage(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> request
    ) {
        String message = request.get("message");
        return ticketService.addMessage(ticketId, message);
    }

    // ✅ GET ALL TICKETS
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // ✅ GET SINGLE TICKET + MESSAGES
    @GetMapping("/{id}")
    public Map<String, Object> getTicketById(@PathVariable Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        List<Message> messages = messageRepository.findByTicketId(id);

        return Map.of(
                "ticket", ticket,
                "messages", messages
        );
    }

    // ✅ GET ONLY MESSAGES
    @GetMapping("/{id}/messages")
    public List<Message> getMessages(@PathVariable Long id) {
        return messageRepository.findByTicketId(id);
    }

    // ✅ UPDATE STATUS
    @PutMapping("/{id}/status")
    public Ticket updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        Status status = Status.valueOf(request.get("status"));
        return ticketService.updateStatus(id, status);
    }

}