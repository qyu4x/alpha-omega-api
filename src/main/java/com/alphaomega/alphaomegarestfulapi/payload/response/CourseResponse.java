package com.alphaomega.alphaomegarestfulapi.payload.response;

import com.alphaomega.alphaomegarestfulapi.entity.CourseCategory;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private String instructorId;

    private String title;

    private String summary;

    private String detailSummary;

    private String bannerUrl;

    private Long totalParticipant;

    private Double rating;

    private String language;

    private PriceResponse price;

    private CourseCategory courseCategory;

    private LessonResponse lessons;

    private RequirementResponse requirements;

}
