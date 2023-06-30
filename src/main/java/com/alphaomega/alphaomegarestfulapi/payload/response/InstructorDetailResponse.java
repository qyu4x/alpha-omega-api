package com.alphaomega.alphaomegarestfulapi.payload.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstructorDetailResponse {

    private InstructorResponse instructor;

    private  UserResponse userResponse;

}
