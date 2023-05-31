package com.alphaomega.alphaomegarestfulapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstructorBiodataRequest {

    @NotBlank(message = "Name can't be empty")
    private String instructorName;

}
