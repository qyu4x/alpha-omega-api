package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseContentResponse {

    private String id;

    private String titleSubCourse;

    private String totalDuration;

    private Integer totalLectures;

    private List<CourseDetailResponse> courseDetails;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
