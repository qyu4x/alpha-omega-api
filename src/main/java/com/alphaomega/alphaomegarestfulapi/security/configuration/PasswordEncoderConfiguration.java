package com.alphaomega.alphaomegarestfulapi.security.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderConfiguration {

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
