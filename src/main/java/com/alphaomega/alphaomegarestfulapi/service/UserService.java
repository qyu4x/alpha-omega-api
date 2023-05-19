package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.*;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import jakarta.mail.MessagingException;

public interface UserService {
    SignupResponse signup(SignupRequest signupRequest) throws MessagingException;
    SigninResponse signin(SigninRequest signinRequest);

    SignupResponse verifyOtp(OtpVerificationRequest otpVerificationRequest);

    SignupResponse refreshVerifyOtp(OtpRefreshCodeRequest otpRefreshCodeRequest) throws MessagingException;

    UserResponse update(UpdateUserRequest updateUserRequest, String userId);
}
