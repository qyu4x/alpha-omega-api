package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseContentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;

import java.util.List;

public interface CourseContentService {

    List<CourseContentResponse> create(List<CourseContentRequest> courseContentRequest, String instructorId, String courseId);

}
