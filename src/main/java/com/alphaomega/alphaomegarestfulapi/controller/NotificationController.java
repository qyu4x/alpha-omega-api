package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.response.CommentResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.NotificationResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.NotificationService;
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
public class NotificationController {

    private final static Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/notification/{userId}")
    public ResponseEntity<WebResponse<List<NotificationResponse>>> findAllNotificationByUserId(@PathVariable("userId") String userId) {
        log.info("Request get all notification for user id {}", userId);

        List<NotificationResponse> notificationResponses = notificationService.findByUserId(userId);
        WebResponse<List<NotificationResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                notificationResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/notification/{notificationId}")
    public ResponseEntity<WebResponse<Boolean>> updateNotificationStatus(@PathVariable("notificationId") String notificationId) {
        log.info("Request update notification for id {}", notificationId);

        Boolean notificationResponses = notificationService.updateIsRead(notificationId);
        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                notificationResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }
}
