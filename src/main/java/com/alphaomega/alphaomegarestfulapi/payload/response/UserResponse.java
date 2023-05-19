package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;

    private String fullName;

    private String email;

    private Set<RoleResponse> roles;

    private String imageUrl;

    private String instructorName;

    private String caption;

    private String biography;

    private Boolean deleted;

    private Boolean isVerify;

    private String webUrl;

    private String facebookUrl;

    private String  linkedinUrl;

    private String youtubeUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
