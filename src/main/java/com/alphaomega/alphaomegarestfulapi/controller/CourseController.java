package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.CourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdateCourseRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.CourseService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alpha/v1")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping("/course/{instructorId}")
    public ResponseEntity<WebResponse<CourseResponse>> createCourse(@PathVariable("instructorId") String instructorId, @Valid @RequestBody CourseRequest courseRequest) {
        log.info("Request create course from instructor id {}", instructorId);
        CourseResponse courseResponse = courseService.create(instructorId, courseRequest);

        WebResponse<CourseResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                courseResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/course/banner/{courseId}")
    public ResponseEntity<WebResponse<CourseResponse>> updateBanner(@RequestPart("image") MultipartFile image, @PathVariable("courseId") String courseId) {
        log.info("Request update banner image course id {}", courseId);
        CourseResponse courseResponse = courseService.updateBanner(image, courseId);

        WebResponse<CourseResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                courseResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/course/{courseId}")
    public ResponseEntity<WebResponse<CourseResponse>> updateBanner(@Valid @RequestBody UpdateCourseRequest courseRequest, @PathVariable("courseId") String courseId) {
        log.info("Request update course  id {}", courseId);
        CourseResponse courseResponse = courseService.update(courseRequest, courseId);

        WebResponse<CourseResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                courseResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<WebResponse<Boolean>> deleteCourse(@PathVariable("courseId") String courseId) {
        log.info("Request update course  id {}", courseId);
        Boolean status = courseService.deleteById(courseId);

        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                status
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<WebResponse<CourseResponse>> findById(@PathVariable("id") String id) {
        log.info("request find course with id {} ", id);
        CourseResponse courseResponse = courseService.findById(id);
        WebResponse<CourseResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                courseResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @GetMapping("/courses")
    public ResponseEntity<WebResponse<Map<String, Object>>> find(@RequestParam(defaultValue = "") String query, @RequestParam(defaultValue = "") String category,
                                                                 @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("request find all course with pageable");
        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> coursePageableResponse = courseService.findAllByTitleAndContentCategoryId(query, category, pageable);
        WebResponse<Map<String, Object>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                coursePageableResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }


}
