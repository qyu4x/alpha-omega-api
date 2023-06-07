package com.alphaomega.alphaomegarestfulapi.payload.response;

import com.alphaomega.alphaomegarestfulapi.entity.CourseCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private String id;

    private String instructorId;

    private String title;

    private String description;

    private String detailDescription;

    private String bannerUrl;

    private Long totalParticipant;

    private Double rating;

    private String language;

    private PriceResponse price;

    private CourseCategoryResponse courseCategory;

    private List<LessonResponse> lessons;

    private List<RequirementResponse> requirements;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
