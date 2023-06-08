package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.PromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.UpdatePromoRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.PromoResponse;

import java.util.List;

public interface PromoService {

    PromoResponse create(PromoRequest promoRequest);

    PromoResponse findById(String id);

    List<PromoResponse> findAll();

    PromoResponse update(UpdatePromoRequest updatePromoRequest, String id);

    Boolean deleteById(String id);
}
