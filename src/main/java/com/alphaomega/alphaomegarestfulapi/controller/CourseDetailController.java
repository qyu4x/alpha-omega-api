package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseContentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseContentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.CourseDetailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alpha/v1")
public class CourseDetailController {

    private static final Logger log = LoggerFactory.getLogger(CourseDetailController.class);

    private CourseDetailService courseDetailService;

    public CourseDetailController(CourseDetailService courseDetailService) {
        this.courseDetailService = courseDetailService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping("/course-detail/{courseContentId}/course/{courseId}")
    public ResponseEntity<WebResponse<CourseDetailResponse>> create(@RequestPart("title") String title,
                                                                    @RequestPart("video") MultipartFile video,
                                                                    @PathVariable("courseId") String courseId,
                                                                    @PathVariable("courseContentId") String courseContentId) throws IOException {
        log.info("Request upload video content detail");
        CourseDetailResponse courseDetailResponse = courseDetailService.create(title, video, courseId, courseContentId);
        WebResponse<CourseDetailResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                courseDetailResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(webResponse);
    }
}
