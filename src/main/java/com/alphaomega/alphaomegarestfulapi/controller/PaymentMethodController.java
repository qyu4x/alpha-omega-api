package com.alphaomega.alphaomegarestfulapi.controller;


import com.alphaomega.alphaomegarestfulapi.payload.response.PaymentMethodResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.PaymentMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alpha/v1")
public class PaymentMethodController {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodController.class);

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/payments")
    public ResponseEntity<WebResponse<List<PaymentMethodResponse>>> findAll() {
        log.info("Request - find all payment method");
        List<PaymentMethodResponse> paymentMethodResponses = paymentMethodService.findAll();
        WebResponse<List<PaymentMethodResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                paymentMethodResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<WebResponse<PaymentMethodResponse>> findById(@PathVariable("id") String id) {
        log.info("Request - find payment method by id");
        PaymentMethodResponse paymentMethodResponse = paymentMethodService.findById(id);
        WebResponse<PaymentMethodResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                paymentMethodResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }
}
