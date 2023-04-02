package com.alphaomega.alphaomegarestfulapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification_category")
public class NotificationCategory {

    @Id
    private String id;

    @Size(max = 20)
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "notificationCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

}
