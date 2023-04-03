package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.SignupRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.SignupResponse;

public interface UserService {
    SignupResponse signup(SignupRequest signupRequest);
}
