package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private String id;

    private String category;

    private String title;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
