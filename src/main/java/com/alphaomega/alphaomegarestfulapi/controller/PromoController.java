package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.request.PromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdatePromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.PromoResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.PromoService;
import jakarta.validation.Valid;
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
public class PromoController {

    private static final Logger log = LoggerFactory.getLogger(PromoController.class);

    private final PromoService promoService;

    public PromoController(PromoService promoService) {
        this.promoService = promoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/promo")
    public ResponseEntity<WebResponse<PromoResponse>> create(@Valid @RequestBody PromoRequest promoRequest) {
        log.info("Request create new promo");
        PromoResponse promoResponse = promoService.create(promoRequest);

        WebResponse<PromoResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                promoResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/promo/{id}")
    public ResponseEntity<WebResponse<PromoResponse>> findById(@PathVariable("id") String id) {
        log.info("Request get all promo");
        PromoResponse promoResponse = promoService.findById(id);

        WebResponse<PromoResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/promos")
    public ResponseEntity<WebResponse<List<PromoResponse>>> getAll() {
        log.info("Request get all promo");
        List<PromoResponse> promoResponses = promoService.findAll();

        WebResponse<List<PromoResponse>> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoResponses
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("promo/{id}")
    public ResponseEntity<WebResponse<PromoResponse>> updatePromo(@Valid @RequestBody UpdatePromoRequest updatePromoRequest, @PathVariable("id") String id) {
        log.info("Request update promo");
        PromoResponse promoResponse = promoService.update(updatePromoRequest, id);

        WebResponse<PromoResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                promoResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("promo/{id}")
    public ResponseEntity<WebResponse<Boolean>> deletePromo(@PathVariable("id") String id) {
        log.info("Request delete promo");
        Boolean deleteResponse = promoService.deleteById(id);

        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }
}
