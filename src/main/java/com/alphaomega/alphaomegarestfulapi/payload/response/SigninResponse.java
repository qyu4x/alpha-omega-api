package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninResponse {

    private String id;

    private String fullName;

    private String email;

    private List<RoleResponse> roles;

    private JwtResponse jwtResponse;

    private String imageUrl;

    private String instructorName;

    private String biography;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
