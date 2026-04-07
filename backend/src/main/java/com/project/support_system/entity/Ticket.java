package com.project.support_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String query;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

}
