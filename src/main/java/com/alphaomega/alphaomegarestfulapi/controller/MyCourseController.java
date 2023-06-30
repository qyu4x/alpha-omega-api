package com.alphaomega.alphaomegarestfulapi.controller;


import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.NotificationResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.MyCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alpha/v1")
public class MyCourseController {

    private final static Logger log = LoggerFactory.getLogger(MyCourseController.class);

    private final MyCourseService myCourseService;

    public MyCourseController(MyCourseService myCourseService) {
        this.myCourseService = myCourseService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/mycourse/{userId}")
    public ResponseEntity<WebResponse<List<CourseResponse>>> findAllMyCourseByUserId(@PathVariable("userId") String userId) {
        log.info("Request get all course for user id {}", userId);

        List<CourseResponse> myCourseResponses = myCourseService.findAll(userId);
        WebResponse<List<CourseResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                myCourseResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
