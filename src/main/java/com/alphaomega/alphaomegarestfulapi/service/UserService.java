package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.OtpRefreshCodeRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OtpVerificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SigninRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;
import jakarta.mail.MessagingException;

public interface UserService {
    SignupResponse signup(SignupRequest signupRequest) throws MessagingException;
    SigninResponse signin(SigninRequest signinRequest);

    SignupResponse verifyOtp(OtpVerificationRequest otpVerificationRequest);

    SignupResponse refreshVerifyOtp(OtpRefreshCodeRequest otpRefreshCodeRequest) throws MessagingException;
}
