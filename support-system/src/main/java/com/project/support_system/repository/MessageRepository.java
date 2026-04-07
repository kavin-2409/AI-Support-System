package com.project.support_system.repository;

import com.project.support_system.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTicketId(Long ticketId);
}
