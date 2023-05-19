package com.alphaomega.alphaomegarestfulapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Name can't be empty")
    private String fullName;

    private String caption;

    private String biography;

    @URL
    private String webUrl;

    @URL
    private String facebookUrl;

    @URL
    private String  linkedinUrl;

    @URL
    private String youtubeUrl;

}
