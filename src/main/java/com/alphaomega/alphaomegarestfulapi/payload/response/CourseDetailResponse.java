package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetailResponse {

    private String id;

    private String title;

    private String videoUrl;

    private String duration;

    Boolean isLocked;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
