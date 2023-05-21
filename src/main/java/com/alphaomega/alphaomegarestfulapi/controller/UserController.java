package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.UpdateUserRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alpha/v1/user")
public class UserController {

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<UserResponse>> getUserInformation(@PathVariable("id") String id) {
        log.info("request find user id {} ", id);
        UserResponse userResponse = userService.findById(id);
        WebResponse<UserResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }


    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<UserResponse>> updateInformation(@RequestBody @Valid UpdateUserRequest updateUserRequest, @PathVariable("id") String id) {
        log.info("request update from user id {} ", id);
        UserResponse userResponse = userService.update(updateUserRequest, id);
        WebResponse<UserResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/avatar/{id}")
    public ResponseEntity<WebResponse<UserResponse>> updateAvatar(@RequestPart("image")MultipartFile image, @PathVariable("id") String id) {
        log.info("request update from user id {} ", id);
        UserResponse userResponse = userService.updateProfile(image, id);
        WebResponse<UserResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                userResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
