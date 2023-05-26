package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoteResponse {

    private UserResponse user;

    private InstructorResponse instructor;


}
