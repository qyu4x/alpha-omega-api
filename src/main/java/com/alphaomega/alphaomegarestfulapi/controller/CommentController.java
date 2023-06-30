package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.CommentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CommentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.CourseCategoryResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.CommentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alpha/v1")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping("comment/{userId}/{courseId}")
    public ResponseEntity<WebResponse<CommentResponse>> postComment(@Valid @RequestBody CommentRequest commentRequest, @PathVariable("userId") String userId, @PathVariable("courseId") String courseId) {
        log.info("Request post comment");

        CommentResponse commentResponse = commentService.create(commentRequest, userId, courseId);
        WebResponse<CommentResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                commentResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

    @GetMapping("/comments/{courseId}")
    public ResponseEntity<WebResponse<List<CommentResponse>>> findAllCommentByCourseId(@PathVariable("courseId") String courseId) {
        log.info("Request get all course comment with id course {}", courseId);

        List<CommentResponse> courseCommentResponses = commentService.findByCourseId(courseId);
        WebResponse<List<CommentResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                courseCommentResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/comment/{userId}/{commentId}")
    public ResponseEntity<WebResponse<Boolean>> deleteComment(@PathVariable("userId") String userId, @PathVariable("commentId") String commentId) {
        log.info("Request delete course  id {}", commentId);

        Boolean status = commentService.deleteById(userId, commentId);
        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                status
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }
}
