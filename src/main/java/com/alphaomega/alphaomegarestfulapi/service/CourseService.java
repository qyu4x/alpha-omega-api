package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdateCourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CourseService {

    CourseResponse create(String instructorId,  CourseRequest courseRequest);

    CourseResponse updateBanner(MultipartFile multipartFile, String courseId);

    CourseResponse update(UpdateCourseRequest updateCourseRequest, String courseId);

    Boolean deleteById(String courseId);

}
