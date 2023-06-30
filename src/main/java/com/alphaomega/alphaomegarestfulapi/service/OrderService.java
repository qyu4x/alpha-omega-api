package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.OrderRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.OrderResponse;
import jakarta.mail.MessagingException;

public interface OrderService {

    OrderResponse create(OrderRequest orderRequest, String userId) throws MessagingException;

}
