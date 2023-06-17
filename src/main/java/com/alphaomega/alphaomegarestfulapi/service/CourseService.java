package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdateCourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CourseService {

    CourseResponse create(String instructorId,  CourseRequest courseRequest);

    CourseResponse updateBanner(MultipartFile multipartFile, String courseId);

    CourseResponse update(UpdateCourseRequest updateCourseRequest, String courseId);

    CourseResponse findById(String courseId);

    Map<String, Object> findAllByTitleAndContentCategoryId(String title, String contentCategoryId, Pageable pagable);


    Boolean deleteById(String courseId);

}
