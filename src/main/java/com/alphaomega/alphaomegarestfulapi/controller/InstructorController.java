package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.InstructorBiodataRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.*;
import com.alphaomega.alphaomegarestfulapi.service.InstructorService;
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
@RequestMapping(path = "/api/alpha/v1")
public class InstructorController {

    private final static Logger log = LoggerFactory.getLogger(InstructorController.class);
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/instructor/{id}")
    public ResponseEntity<WebResponse<InstructorDetailResponse>> findById(@PathVariable("id") String id) {
        log.info("Request get instructor data");
        InstructorDetailResponse instructorDetailResponse = instructorService.findById(id);

        WebResponse<InstructorDetailResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                instructorDetailResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/instructors")
    public ResponseEntity<WebResponse<List<InstructorDetailResponse>>> findAll() {
        log.info("Request get all instructor data");
        List<InstructorDetailResponse> instructorDetailResponses = instructorService.findAll();

        WebResponse<List<InstructorDetailResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                instructorDetailResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/instructor/{id}")
    public ResponseEntity<WebResponse<UserResponse>> updateInstructorInformation(@PathVariable("id") String id, @Valid @RequestBody InstructorBiodataRequest instructorBiodataRequest) {
        log.info("Request update instructor data");
        UserResponse userResponse = instructorService.updateInstructorName(id, instructorBiodataRequest);

        WebResponse<UserResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }
}
