package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BannerService {

    BannerResponse create(MultipartFile file);
    BannerResponse update(MultipartFile file, String bannerId);
    List<BannerResponse> findAll();

    Boolean delete(String bannerId);

}
