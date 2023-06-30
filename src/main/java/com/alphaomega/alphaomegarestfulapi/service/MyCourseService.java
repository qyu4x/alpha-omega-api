package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyCourseService {

    List<CourseResponse> findAll(String userId);

}
