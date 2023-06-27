package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.NotificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void pushOrder(NotificationRequest notificationRequest, String userId);

    List<NotificationResponse> findByUserId(String userId);

    Boolean updateIsRead(String id);

}
