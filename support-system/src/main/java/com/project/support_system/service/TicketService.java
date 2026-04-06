package com.project.support_system.service;
import com.project.support_system.entity.Message;
import com.project.support_system.entity.Sender;
import com.project.support_system.entity.Status;
import com.project.support_system.entity.Ticket;
import com.project.support_system.repository.MessageRepository;
import com.project.support_system.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MessageRepository messageRepository;
    public String createTicket(String query) {

        // 1. Create ticket
        Ticket ticket = new Ticket();
        ticket.setQuery(query);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);

        // 2. Store user message
        Message userMessage = new Message();
        userMessage.setTicketId(savedTicket.getId());
        userMessage.setSender(Sender.USER);
        userMessage.setMessage(query);
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // 3. Generate AI response (mock)
        String aiResponse;

        try {
            aiResponse = openAIService.getAIResponse(query);
        } catch (Exception e) {
            aiResponse = generateFallbackResponse(query);
        }

        // 4. Store AI message
        Message aiMessage = new Message();
        aiMessage.setTicketId(savedTicket.getId());
        aiMessage.setSender(Sender.AI);
        aiMessage.setMessage(aiResponse);
        aiMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(aiMessage);

        // 5. Return response
        return aiResponse;
    }
    private String generateAIResponse(String query) {

        if (query.toLowerCase().contains("order")) {
            return "Please provide your order ID to assist you.";
        } else if (query.toLowerCase().contains("refund")) {
            return "Your request has been forwarded to our support team.";
        } else {
            return "Thank you for contacting us. How can I assist further?";
        }
    }
    private String generateFallbackResponse(String query) {
        if (query.toLowerCase().contains("order")) {
            return "Please provide your order ID to assist you.";
        } else if (query.toLowerCase().contains("refund")) {
            return "Your request has been forwarded to our support team.";
        } else {
            return "Thank you for contacting us. Our support team will assist you shortly.";
        }
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
}