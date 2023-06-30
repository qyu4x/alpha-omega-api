package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.WishListResponse;

import java.util.List;

public interface WishListService {

    WishListResponse save(String userId, String courseId);

    List<WishListResponse> findAllById(String userId);

    Boolean deleteById(String id);
}
