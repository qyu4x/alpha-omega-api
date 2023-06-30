package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.response.CourseCategoryResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.InstructorDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.CourseCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/alpha/v1")
public class CourseCategoryController {

    private final static Logger log = LoggerFactory.getLogger(CourseCategoryController.class);

    private final CourseCategoryService courseCategoryService;

    public CourseCategoryController(CourseCategoryService courseCategoryService) {
        this.courseCategoryService = courseCategoryService;
    }

    @GetMapping("/course-categories")
    public ResponseEntity<WebResponse<List<CourseCategoryResponse>>> findAll() {
        log.info("Request get all course categories data");
        List<CourseCategoryResponse> courseCategoryResponses = courseCategoryService.findAll();

        WebResponse<List<CourseCategoryResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                courseCategoryResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
