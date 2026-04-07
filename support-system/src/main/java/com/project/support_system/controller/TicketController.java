package com.project.support_system.controller;

import com.project.support_system.entity.Message;
import com.project.support_system.entity.Ticket;
import com.project.support_system.repository.MessageRepository;
import com.project.support_system.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.support_system.entity.Status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    public String createTicket(@RequestBody Map<String,String> request) {
        return ticketService.createTicket(request.get("query"));
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public Optional<Ticket> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }
    @GetMapping("/{id}/messages")
    public List<Message> getMessages(@PathVariable Long id) {
        return messageRepository.findByTicketId(id);
    }

    @PutMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Status status = Status.valueOf(request.get("status"));
        return ticketService.updateStatus(id, status);
    }
}