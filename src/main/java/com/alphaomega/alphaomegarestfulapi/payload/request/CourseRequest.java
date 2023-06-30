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

    @NotBlank(message = "Course category id can't be empty")
    private String courseCategoryId;

    @NotBlank(message = "Title can't be empty")
    private String title;

    @NotBlank(message = "Description can't be empty")
    private String description;

    @NotBlank(message = "Detail description can't be empty")
    private String detailDescription;

    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotBlank(message = "Support language can't be empty")
    private String language;

    @Valid
    private List<LessonRequest> lessons;

    @Valid
    private List<RequirementRequest> requirements;

}
