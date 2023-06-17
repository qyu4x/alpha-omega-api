package com.alphaomega.alphaomegarestfulapi.payload.request;

import com.alphaomega.alphaomegarestfulapi.entity.CourseDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseContentRequest {

    @NotBlank(message = "Title sub course can't be empty")
    private String titleSubCourse;

}
