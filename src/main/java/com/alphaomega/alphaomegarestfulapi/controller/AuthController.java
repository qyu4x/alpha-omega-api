package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.SigninRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alpha/v1/auth")
public class AuthController {

    private final static Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<WebResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest signupRequest) {
        log.info("Do signup process for email {} ", signupRequest.getEmail());
        SignupResponse signupResponse = userService.signup(signupRequest);
        WebResponse<SignupResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                signupResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PostMapping(path = "/signin")
    public ResponseEntity<WebResponse<SigninResponse>> signin(@RequestBody @Valid SigninRequest signinRequest) {
        log.info("Do signin process for email {} ", signinRequest.getEmail());
        SigninResponse signinResponse = userService.signin(signinRequest);
        WebResponse<SigninResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                signinResponse
        );
        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
