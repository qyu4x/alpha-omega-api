package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "notification_category_id")
    private NotificationCategory notificationCategory;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Boolean isRead = Boolean.FALSE;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
