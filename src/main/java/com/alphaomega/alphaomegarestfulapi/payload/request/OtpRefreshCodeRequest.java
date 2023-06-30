package com.alphaomega.alphaomegarestfulapi.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpRefreshCodeRequest {

    @Email
    @NotBlank(message = "Email can't be empty")
    private String email;

}
