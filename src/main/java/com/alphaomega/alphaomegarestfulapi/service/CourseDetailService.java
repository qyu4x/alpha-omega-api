package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseDetailResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseDetailService {

    CourseDetailResponse create(String title, MultipartFile videoSource, String courseId, String courseContentId) throws IOException;

    Boolean deleteById(String courseDetailId);

    CourseDetailResponse update(String title, MultipartFile videoSource, String courseDetailId) throws IOException;

}
