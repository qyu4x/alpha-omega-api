package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponse {

    private String id;

    private String fullName;

    private String email;

    private Set<RoleResponse> roles;

    private JwtResponse token;

    private String imageUrl;

    private String instructorName;

    private String biography;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
