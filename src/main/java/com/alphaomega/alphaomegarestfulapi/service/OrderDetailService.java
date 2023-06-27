package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.entity.Order;
import com.alphaomega.alphaomegarestfulapi.payload.request.OrderDetailRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CreateOrderDetailResponse;

import java.util.List;

public interface OrderDetailService {
    CreateOrderDetailResponse createOrderDetail(List<OrderDetailRequest> orderDetailRequests, Order order, String promoId);
}
