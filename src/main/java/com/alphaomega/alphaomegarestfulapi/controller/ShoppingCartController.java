package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.response.ShoppingCartResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.ShoppingCartService;
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
public class ShoppingCartController {

    private final static Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/cart/{userId}/course/{courseId}")
    public ResponseEntity<WebResponse<ShoppingCartResponse>> createShoppingCart(@PathVariable("userId") String userId, @PathVariable("courseId") String courseId) {
        log.info("Request create new shopping cart");

        ShoppingCartResponse shoppingCartResponse = shoppingCartService.save(userId, courseId);
        WebResponse<ShoppingCartResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                shoppingCartResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @GetMapping("/cart/{userId}")
    public ResponseEntity<WebResponse<List<ShoppingCartResponse>>> getAllWishlist(@PathVariable("userId") String userId) {
        log.info("request find shopping cart for user id {} ", userId);

        List<ShoppingCartResponse> shoppingCartResponses = shoppingCartService.findAllByUserId(userId);
        WebResponse<List<ShoppingCartResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                shoppingCartResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<WebResponse<Boolean>> delete(@PathVariable("id") String id) {
        log.info("request delete shopping cart with id {} ", id);
        Boolean deleteResponse = shoppingCartService.deleteById(id);
        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

}
