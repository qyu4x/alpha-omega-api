package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.ShoppingCartResponse;

import java.util.List;

public interface ShoppingCartService {

    ShoppingCartResponse save(String userId, String courseId);

    List<ShoppingCartResponse> findAllByUserId(String userId);

    Boolean deleteById(String shoppingCartId);

}
