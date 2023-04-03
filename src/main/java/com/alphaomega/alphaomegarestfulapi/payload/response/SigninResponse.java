package com.alphaomega.alphaomegarestfulapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SigninResponse {

    private String id;

    private String fullName;

    private String email;

    private List<RoleResponse> roles;

    private String imageUrl;

    private String instructorName;

    private String biography;

    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
