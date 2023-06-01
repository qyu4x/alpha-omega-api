package com.alphaomega.alphaomegarestfulapi.controller;

import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.WebResponse;
import com.alphaomega.alphaomegarestfulapi.service.BannerService;
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
@RequestMapping("/api/alpha/v1/banner")
public class BannerController {

    private final static Logger log = LoggerFactory.getLogger(BannerController.class);

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<WebResponse<BannerResponse>> createBanner(@RequestPart("banner") MultipartFile banner) {
        log.info("Request upload banner image");
        BannerResponse bannerResponse = bannerService.create(banner);

        WebResponse<BannerResponse> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                bannerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<Boolean>> deleteBanner(@PathVariable("id") String id) {
        log.info("Request delete banner image");
        Boolean deleteResponse = bannerService.delete(id);

        WebResponse<Boolean> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                deleteResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<BannerResponse>> updateBanner(@RequestPart("banner") MultipartFile banner, @PathVariable("id") String id) {
        log.info("Request upload banner image");
        BannerResponse bannerResponse = bannerService.update(banner, id);

        WebResponse<BannerResponse> webResponse = new WebResponse<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                bannerResponse
        );

        return ResponseEntity.status(HttpStatus.OK).body(webResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<WebResponse<List<BannerResponse>>> getAll() {
        log.info("Request upload banner image");
        List<BannerResponse> bannerResponses = bannerService.findAll();

        WebResponse<List<BannerResponse>> webResponse = new WebResponse<>(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                bannerResponses
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(webResponse);
    }
}
