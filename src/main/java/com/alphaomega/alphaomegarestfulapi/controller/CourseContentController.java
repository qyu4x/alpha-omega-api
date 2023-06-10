package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseContentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.CourseContentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/alpha/v1")
public class CourseContentController {

    private static final Logger log = LoggerFactory.getLogger(CourseContentController.class);

    private CourseContentService courseContentService;

    public CourseContentController(CourseContentService courseContentService) {
        this.courseContentService = courseContentService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping("/content/{instructorId}/course/{courseId}")
    public ResponseEntity<WebResponse<List<CourseContentResponse>>> create(@Valid @RequestBody List<CourseContentRequest> courseContentRequests,
                                                                           @PathVariable("instructorId") String instructorId,
                                                                           @PathVariable("courseId") String courseId) {
        log.info("Request create content or instructor id {}", instructorId);
        List<CourseContentResponse> courseContentResponses = courseContentService.create(courseContentRequests, instructorId, courseId);
        WebResponse<List<CourseContentResponse>> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                courseContentResponses
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(webResponse);
    }
}
