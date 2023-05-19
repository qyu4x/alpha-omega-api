package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.OtpRefreshCodeRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OtpVerificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SigninRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alpha/v1/auth")
public class AuthController {

    private final static Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;


    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<WebResponse<EntityModel<SignupResponse>>> signup(@RequestBody @Valid SignupRequest signupRequest) throws MessagingException {
        log.info("Do signup process for email {} ", signupRequest.getEmail());
        SignupResponse signupResponse = userService.signup(signupRequest);

        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).verifyOtp(null)).withRel("verifyOtp");
        EntityModel<SignupResponse> signupResponseEntityModel = EntityModel.of(signupResponse, link);
        WebResponse<EntityModel<SignupResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                signupResponseEntityModel
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<WebResponse<SignupResponse>> verifyOtp(@RequestBody @Valid OtpVerificationRequest otpVerificationRequest) {
        log.info("Do verify process for email {} ", otpVerificationRequest.getEmail());
        SignupResponse signupVerifyResponse = userService.verifyOtp(otpVerificationRequest);
        WebResponse<SignupResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                signupVerifyResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PostMapping(path = "/refresh-otp")
    public ResponseEntity<WebResponse<SignupResponse>> refreshOtp(@RequestBody @Valid OtpRefreshCodeRequest otpRefreshCodeRequest) throws MessagingException {
        log.info("Do refresh verify process for email {} ", otpRefreshCodeRequest.getEmail());
        SignupResponse signupRefreshVerifyCodeResponse = userService.refreshVerifyOtp(otpRefreshCodeRequest);
        WebResponse<SignupResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                signupRefreshVerifyCodeResponse
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
