package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.UserResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WishListResponse;
import com.alphaomega.alphaomegarestfulapi.repository.WishListRepository;
import com.alphaomega.alphaomegarestfulapi.service.WishListService;
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
public class WishListController {

    private static final Logger log = LoggerFactory.getLogger(WishListController.class);

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/wish-list/{userId}/course/{courseId}")
    public ResponseEntity<WebResponse<WishListResponse>> createWishList(@PathVariable("userId") String userId, @PathVariable("courseId") String courseId) {
        log.info("Request create new wishlist");

        WishListResponse wishListResponse = wishListService.save(userId, courseId);
        WebResponse<WishListResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                wishListResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/wish-list/{userId}")
    public ResponseEntity<WebResponse<List<WishListResponse>>> getAllWishlist(@PathVariable("userId") String userId) {
        log.info("request find wish list for user id {} ", userId);

        List<WishListResponse> wishListResponses = wishListService.findAllById(userId);
        WebResponse<List<WishListResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                wishListResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @DeleteMapping("/wish-list/{id}")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("id") String id) {
        log.info("request delete wish list with id {} ", id);
        Boolean deleteResponse = wishListService.deleteById(id);
        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
