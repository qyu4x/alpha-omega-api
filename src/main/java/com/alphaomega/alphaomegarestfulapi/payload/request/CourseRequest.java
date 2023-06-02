package com.alphaomega.alphaomegarestfulapi.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    @NotBlank(message = "Instructor id can't be empty")
    private String instructorId;

    @NotBlank(message = "Course category id can't be empty")
    private String courseCategoryId;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotBlank(message = "Summary can't be empty")
    private String summary;

    @NotBlank(message = "Detail summary can't be empty")
    private String detailSummary;

    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotBlank(message = "Support language can't be empty")
    private String language;

    @Valid
    private List<LessonRequest> lessons;

    @Valid
    private List<RequirementRequest> requests;

}
