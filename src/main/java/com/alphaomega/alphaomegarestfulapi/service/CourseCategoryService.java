package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.CourseCategoryResponse;

import java.util.List;

public interface CourseCategoryService {

    List<CourseCategoryResponse> findAll();

}
