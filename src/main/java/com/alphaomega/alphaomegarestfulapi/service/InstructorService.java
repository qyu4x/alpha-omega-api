package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.InstructorBiodataRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.InstructorDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;

import java.util.List;

public interface InstructorService {

    InstructorDetailResponse findById(String instructorId);

    List<InstructorDetailResponse> findAll();

    UserResponse updateInstructorName(String instructorId, InstructorBiodataRequest instructorBiodataRequest);


}
