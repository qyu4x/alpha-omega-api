package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Banner;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.response.BannerResponse;
import com.alphaomega.alphaomegarestfulapi.repository.BannerRepository;
import com.alphaomega.alphaomegarestfulapi.service.BannerService;
import com.alphaomega.alphaomegarestfulapi.service.FirebaseCloudStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BannerServiceImpl implements BannerService {

    private final static Logger log = LoggerFactory.getLogger(BannerServiceImpl.class);

    private BannerRepository bannerRepository;

    private FirebaseCloudStorageService firebaseCloudStorageService;

    public BannerServiceImpl(BannerRepository bannerRepository, FirebaseCloudStorageService firebaseCloudStorageService) {
        this.bannerRepository = bannerRepository;
        this.firebaseCloudStorageService = firebaseCloudStorageService;
    }

    @Override
    public BannerResponse create(MultipartFile file) {
        log.info("Perform upload image banner");
        if (file.isEmpty()) {
            throw new DataNotFoundException("Image cannot be empty");
        }

        String bannerUrl = firebaseCloudStorageService.doUploadImageFile(file);
        Banner banner = Banner.builder()
                .id("bb-".concat(UUID.randomUUID().toString()))
                .imageUrl(bannerUrl)
                .createdAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                .updatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime())
                .build();

        bannerRepository.save(banner);
        log.info("Successfully  upload image banner");
        return BannerResponse.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .createdAt(banner.getCreatedAt())
                .updatedAt(banner.getUpdatedAt())
                .build();
    }

    @Transactional
    @Override
    public BannerResponse update(MultipartFile file, String bannerId) {
        log.info("Perform update image banner");
        if (file.isEmpty()) {
            throw new DataNotFoundException("Image cannot be empty");
        }

        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new DataNotFoundException("Data banner not found"));
        String bannerUrl = firebaseCloudStorageService.doUploadImageFile(file);
        banner.setImageUrl(bannerUrl);

        bannerRepository.save(banner);
        log.info("Successfully update image banner");
        return BannerResponse.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .createdAt(banner.getCreatedAt())
                .updatedAt(banner.getUpdatedAt())
                .build();
    }

    @Override
    public List<BannerResponse> findAll() {
        log.info("Perform get all image banner");
        List<Banner> banners = bannerRepository.findAll();

        List<BannerResponse> bannerResponses = new ArrayList<>();
        banners.forEach(banner -> {
            BannerResponse bannerResponse = BannerResponse.builder()
                    .id(banner.getId())
                    .imageUrl(banner.getImageUrl())
                    .createdAt(banner.getCreatedAt())
                    .updatedAt(banner.getUpdatedAt())
                    .build();
            bannerResponses.add(bannerResponse);
        });

        log.info("Successfully get all image banner");
        return bannerResponses;
    }
}
