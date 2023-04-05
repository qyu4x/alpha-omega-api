package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.SigninRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.SigninResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;

public interface UserService {
    SignupResponse signup(SignupRequest signupRequest);
    SigninResponse signin(SigninRequest signinRequest);
}
