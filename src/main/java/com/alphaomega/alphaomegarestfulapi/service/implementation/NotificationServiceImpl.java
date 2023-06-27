package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Notification;
import com.alphaomega.alphaomegarestfulapi.entity.NotificationCategory;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.NotificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.NotificationResponse;
import com.alphaomega.alphaomegarestfulapi.repository.NotificationCategoryRepository;
import com.alphaomega.alphaomegarestfulapi.repository.NotificationRepository;
import com.alphaomega.alphaomegarestfulapi.repository.UserRepository;
import com.alphaomega.alphaomegarestfulapi.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private NotificationCategoryRepository notificationCategoryRepository;

    private NotificationRepository notificationRepository;

    private UserRepository userRepository;

    public NotificationServiceImpl(NotificationCategoryRepository notificationCategoryRepository, NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationCategoryRepository = notificationCategoryRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void pushOrder(NotificationRequest notificationRequest, String userId) {
        log.info("create push notification for user id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        NotificationCategory cateogry = notificationCategoryRepository
                .findNotificationCategoryByName("Transaction");
        Notification notification = new Notification();
        notification.setId("unn-".concat(UUID.randomUUID().toString()));
        notification.setTitle(notificationRequest.getTitle());
        notification.setMessage(notificationRequest.getMessage());
        notification.setIsRead(false);
        notification.setNotificationCategory(cateogry);
        notification.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        notification.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        notification.setUser(user);

        notificationRepository.save(notification);
        log.info("Successfully create notification");
    }

    @Override
    @Transactional
    public List<NotificationResponse> findByUserId(String userId) {
        log.info("Find all notification for user id {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<NotificationResponse> notificationResponses = new ArrayList<>();

        notifications.stream().forEach(notification -> {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setId(notification.getId());
            notificationResponse.setTitle(notification.getTitle());
            notificationResponse.setIsRead(notification.getIsRead());
            notificationResponse.setMessage(notification.getMessage());
            notificationResponse.setCategory(notification.getNotificationCategory().getName());
            notificationResponse.setCreatedAt(notification.getCreatedAt());
            notificationResponse.setUpdatedAt(notification.getUpdatedAt());

            notificationResponses.add(notificationResponse);
        });
        log.info("Successfully get all user notification");
        return notificationResponses;
    }

    @Override
    public Boolean updateIsRead(String id) {
        log.info("Update notification status");
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Notification not found"));
        notification.setIsRead(true);
        notification.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        notificationRepository.save(notification);
        log.info("Successfully update notification status");
        return true;
    }
}
