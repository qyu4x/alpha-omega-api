package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorResponse {

    private String id;

    private String userId;

    private Long totalParticipant;

    private Long totalReview;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
