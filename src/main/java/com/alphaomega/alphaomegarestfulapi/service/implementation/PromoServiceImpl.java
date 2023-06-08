package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Promo;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.PromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdatePromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.PriceResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.PromoResponse;
import com.alphaomega.alphaomegarestfulapi.repository.PromoRepository;
import com.alphaomega.alphaomegarestfulapi.service.PromoService;
import com.alphaomega.alphaomegarestfulapi.util.CurrencyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PromoServiceImpl implements PromoService {

    private static final Logger log = LoggerFactory.getLogger(PromoServiceImpl.class);

    private PromoRepository promoRepository;

    public PromoServiceImpl(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @Override
    public PromoResponse create(PromoRequest promoRequest) {
        log.info("Create new promo code");
        if (!promoRepository.findPromoByCode(promoRequest.getCode()).isEmpty()) {
            throw new DataAlreadyExistsException("Promo code already exist");
        }

        Promo promo = new Promo();
        promo.setId("pr-".concat(UUID.randomUUID().toString()));
        promo.setCode(promoRequest.getCode());
        promo.setDescription(promoRequest.getDescription());
        promo.setTotalDiscount(promoRequest.getTotalDiscount());
        promo.setExpirationDate(promoRequest.getExpirationDate());
        promo.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        promo.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        promoRepository.save(promo);
        log.info("Successfully create new promo code");

        PriceResponse totalDiscount = new PriceResponse();
        totalDiscount.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());
        totalDiscount.setAmount(promo.getTotalDiscount());
        totalDiscount.setDisplay(CurrencyUtil.convertToDisplayCurrency(promo.getTotalDiscount()));

        PromoResponse promoResponse = new PromoResponse();
        promoResponse.setId(promo.getId());
        promoResponse.setCode(promo.getCode());
        promoResponse.setDescription(promo.getDescription());
        promoResponse.setTotalDiscount(totalDiscount);
        promoResponse.setCreatedAt(promo.getCreatedAt());
        promoResponse.setUpdatedAt(promo.getUpdatedAt());

        return promoResponse;
    }

    @Transactional
    @Override
    public PromoResponse findById(String id) {
        log.info("Find promo code");
        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promo code not found"));

        if (promo.getExpirationDate().atTime(LocalTime.of(0, 0)).isBefore(LocalDateTime.now())) {
            throw new DataNotFoundException("Oops promo code not found");
        }
        log.info("Successfully get promo code");

        PriceResponse totalDiscount = new PriceResponse();
        totalDiscount.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());
        totalDiscount.setAmount(promo.getTotalDiscount());
        totalDiscount.setDisplay(CurrencyUtil.convertToDisplayCurrency(promo.getTotalDiscount()));

        PromoResponse promoResponse = new PromoResponse();
        promoResponse.setId(promo.getId());
        promoResponse.setCode(promo.getCode());
        promoResponse.setDescription(promo.getDescription());
        promoResponse.setTotalDiscount(totalDiscount);
        promoResponse.setCreatedAt(promo.getCreatedAt());
        promoResponse.setUpdatedAt(promo.getUpdatedAt());

        return promoResponse;
    }

    @Transactional
    @Override
    public List<PromoResponse> findAll() {
        log.info("Find all available promo");
        List<PromoResponse> promoResponses = new ArrayList<>();
        promoRepository.findAll().stream()
                .filter(promo -> promo.getExpirationDate().atTime(0, 0).isAfter(LocalDateTime.now()))
                .forEach(promo -> {
                    PriceResponse totalDiscount = new PriceResponse();
                    totalDiscount.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());
                    totalDiscount.setAmount(promo.getTotalDiscount());
                    totalDiscount.setDisplay(CurrencyUtil.convertToDisplayCurrency(promo.getTotalDiscount()));

                    PromoResponse promoResponse = new PromoResponse();
                    promoResponse.setId(promo.getId());
                    promoResponse.setCode(promo.getCode());
                    promoResponse.setDescription(promo.getDescription());
                    promoResponse.setTotalDiscount(totalDiscount);
                    promoResponse.setCreatedAt(promo.getCreatedAt());
                    promoResponse.setUpdatedAt(promo.getUpdatedAt());

                    promoResponses.add(promoResponse);
                });

        log.info("Successfully find all promo");
        return promoResponses;
    }

    @Transactional
    @Override
    public PromoResponse update(UpdatePromoRequest updatePromoRequest, String id) {
        log.info("Update promo with id {}", id);
        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promo code not found"));

        if (!updatePromoRequest.getCode().equals(promo.getCode()) && !promoRepository.findPromoByCode(updatePromoRequest.getCode()).isEmpty()) {
            throw new DataAlreadyExistsException("Promo code already exist");
        }

        promo.setCode(updatePromoRequest.getCode());
        promo.setTotalDiscount(updatePromoRequest.getTotalDiscount());
        promo.setDescription(updatePromoRequest.getDescription());
        promo.setExpirationDate(updatePromoRequest.getExpirationDate());
        promo.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        promoRepository.save(promo);
        log.info("Successfully update promo with id {}", id);

        PriceResponse totalDiscount = new PriceResponse();
        totalDiscount.setCurrencyCode(CurrencyUtil.getIndonesiaCurrencyCode());
        totalDiscount.setAmount(promo.getTotalDiscount());
        totalDiscount.setDisplay(CurrencyUtil.convertToDisplayCurrency(promo.getTotalDiscount()));

        PromoResponse promoResponse = new PromoResponse();
        promoResponse.setId(promo.getId());
        promoResponse.setCode(promo.getCode());
        promoResponse.setDescription(promo.getDescription());
        promoResponse.setTotalDiscount(totalDiscount);
        promoResponse.setCreatedAt(promo.getCreatedAt());
        promoResponse.setUpdatedAt(promo.getUpdatedAt());

        return promoResponse;
    }

    @Transactional
    @Override
    public Boolean deleteById(String id) {
        log.info("Delete promo with id {}", id);
        Promo promo = promoRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Promo code not found"));
        promoRepository.delete(promo);
        log.info("Successfully delete promo with id {}", id);
        return true;
    }
}
