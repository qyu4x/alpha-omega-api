package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.CourseCategory;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseCategoryResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseCategoryRepository;
import com.alphaomega.alphaomegarestfulapi.service.CourseCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    private final static Logger log = LoggerFactory.getLogger(CourseCategoryServiceImpl.class);

    private CourseCategoryRepository courseCategoryRepository;

    public CourseCategoryServiceImpl(CourseCategoryRepository courseCategoryRepository) {
        this.courseCategoryRepository = courseCategoryRepository;
    }

    @Transactional
    @Override
    public List<CourseCategoryResponse> findAll() {
        log.info("Find all course categories");
        List<CourseCategory> courseCategories = courseCategoryRepository.findAll();

        List<CourseCategoryResponse> courseCategoryResponses = new ArrayList<>();
        courseCategories.stream().forEach(courseCategory -> {
            CourseCategoryResponse courseCategoryResponse = CourseCategoryResponse.builder()
                    .id(courseCategory.getId())
                    .name(courseCategory.getName())
                    .createdAt(courseCategory.getCreatedAt())
                    .updatedAt(courseCategory.getUpdatedAt())
                    .build();
            courseCategoryResponses.add(courseCategoryResponse);
        });
        log.info("Successfully get all course categories");
        return courseCategoryResponses;
    }
}
