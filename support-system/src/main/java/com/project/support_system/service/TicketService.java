package com.project.support_system.service;

import com.project.support_system.entity.Status;
import com.project.support_system.entity.Ticket;
import com.project.support_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTicket(String query) {
        Ticket ticket = new Ticket();
        ticket.setQuery(query);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
}