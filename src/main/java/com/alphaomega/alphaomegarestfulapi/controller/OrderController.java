package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.OrderRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.OrderResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.OrderService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alpha/v1")
public class OrderController {

    private final static Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<WebResponse<OrderResponse>> createBanner(@Valid @RequestBody OrderRequest orderRequest, @PathVariable("userId") String userId) throws MessagingException {
        log.info("Request create order");
        OrderResponse orderResponse = orderService.create(orderRequest, userId);

        WebResponse<OrderResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                orderResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

}
