package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;

public interface CourseService {

    CourseResponse create(CourseRequest courseRequest);

}
