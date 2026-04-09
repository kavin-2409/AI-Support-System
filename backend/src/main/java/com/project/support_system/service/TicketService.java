package com.project.support_system.service;
import com.project.support_system.entity.Message;
import com.project.support_system.entity.Sender;
import com.project.support_system.entity.Status;
import com.project.support_system.entity.Ticket;
import com.project.support_system.entity.User;
import com.project.support_system.repository.UserRepository;
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
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GeminiService geminiService;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MessageRepository messageRepository;
    public String createTicket(String query, String token) {

        // 🔥 1. Extract email from JWT
        String email = jwtService.extractEmail(token);

        // 🔥 2. Fetch user from DB
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 3. Create ticket
        Ticket ticket = new Ticket();
        ticket.setQuery(query);
        ticket.setStatus(Status.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        //ticket.setResponse(aiResponse);

        // 🔥 4. SET USER ID (THIS IS IMPORTANT)
        ticket.setUserId(user.getId());


        //ticketRepository.save(ticket);

        Ticket savedTicket = ticketRepository.save(ticket);

        // 🔥 5. Store user message
        Message userMessage = new Message();
        userMessage.setTicketId(savedTicket.getId());
        userMessage.setSender(Sender.USER);
        userMessage.setMessage(query);
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // 🔥 6. AI response
        //String aiResponse = openAIService.getAIResponse(query);
       // String aiResponse;

       // try {
          //  aiResponse = openAIService.getAIResponse(query);
        //} catch (Exception e) {
         //   aiResponse = generateFallbackResponse(query);
       // }
        String aiResponse = geminiService.getResponse(query);
        if (aiResponse == null || aiResponse.isEmpty()) {
            aiResponse = generateFallbackResponse(query);
        }
        if (aiResponse.contains("support team")) {
            ticket.setStatus(Status.NEEDS_HUMAN); // instead of OPEN
        } else {
            ticket.setStatus(Status.RESOLVED);
        }
        ticketRepository.save(ticket);


        // 🔥 7. Store AI message
        Message aiMessage = new Message();
        aiMessage.setTicketId(savedTicket.getId());
        aiMessage.setSender(Sender.AI);
        aiMessage.setMessage(aiResponse);
        aiMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(aiMessage);

        // 🔥 8. Return response
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

        String q = query.toLowerCase();

        // 📦 ORDER RELATED
        if (q.contains("order") || q.contains("delivery") || q.contains("shipping")) {
            return "Please provide your order ID. You can track your order in the 'My Orders' section.";
        }

        // 💰 REFUND / PAYMENT
        else if (q.contains("refund") || q.contains("return") || q.contains("payment")) {
            return "Your request has been forwarded to our support team. Refunds are typically processed within 5-7 business days.";
        }

        // 🔐 ACCOUNT / PASSWORD
        else if (q.contains("password") || q.contains("login") || q.contains("account")) {
            return "You can reset your password using the 'Forgot Password' option on the login page.";
        }

        // 📞 CONTACT / SUPPORT
        else if (q.contains("contact") || q.contains("support") || q.contains("help")) {
            return "You can reach our support team via email or raise a ticket here. We are happy to assist you!";
        }

        // 🚫 CANCELLATION
        else if (q.contains("cancel") || q.contains("cancellation")) {
            return "Orders can be cancelled within 24 hours of placing them. Please provide your order ID to proceed.";
        }

        // 📍 ADDRESS CHANGE
        else if (q.contains("address") || q.contains("change address")) {
            return "You can update your delivery address from your profile settings before the order is shipped.";
        }

        // ⏰ DELIVERY DELAY
        else if (q.contains("late") || q.contains("delay")) {
            return "We apologize for the delay. Please provide your order ID so we can check the status for you.";
        }

        // 📦 DAMAGED PRODUCT
        else if (q.contains("damaged") || q.contains("broken")) {
            return "We are sorry to hear that. Please upload images of the product and your order ID to initiate a replacement.";
        }
        else if (q.contains("12345")){
            return "We will update about your order sooner.So kindly wait please";
        }

        // 🎯 DEFAULT
        else {
            return "Thank you for contacting us. Our support team will review your request and assist you shortly.";
        }
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
    public Ticket updateStatus(Long ticketId, Status status) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }
}