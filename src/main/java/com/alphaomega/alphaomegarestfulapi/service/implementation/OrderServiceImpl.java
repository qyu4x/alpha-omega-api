package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Notification;
import com.alphaomega.alphaomegarestfulapi.entity.Order;
import com.alphaomega.alphaomegarestfulapi.entity.Promo;
import com.alphaomega.alphaomegarestfulapi.entity.User;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotValidException;
import com.alphaomega.alphaomegarestfulapi.exception.InvalidOtpException;
import com.alphaomega.alphaomegarestfulapi.payload.request.NotificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OrderRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CreateOrderDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.OrderResponse;
import com.alphaomega.alphaomegarestfulapi.repository.*;
import com.alphaomega.alphaomegarestfulapi.security.util.EmailUtils;
import com.alphaomega.alphaomegarestfulapi.service.*;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    private OrderRepository orderRepository;

    private OrderDetailRepository orderDetailRepository;

    private UserRepository userRepository;

    private OrderDetailService orderDetailService;


    private PromoRepository promoRepository;


    private ShoppingCartRepository shoppingCartRepository;


    private WishListRepository wishListRepository;


    private NotificationService notificationService;


    private EmailUtils emailUtils;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, UserRepository userRepository, OrderDetailService orderDetailService, PromoRepository promoRepository, ShoppingCartRepository shoppingCartRepository, WishListRepository wishListRepository, NotificationService notificationService, EmailUtils emailUtils) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.orderDetailService = orderDetailService;
        this.promoRepository = promoRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.wishListRepository = wishListRepository;
        this.notificationService = notificationService;
        this.emailUtils = emailUtils;
    }

    @Transactional
    @Override
    public OrderResponse create(OrderRequest orderRequest, String userId) throws MessagingException {
        log.info("Create order for user id {}", userId);

        if (orderRequest.getPromoId() == null) {
            orderRequest.setPromoId("NA");
        }
        Optional<Promo> promo = promoRepository.findById(orderRequest.getPromoId());
        if (promo.isPresent() && promo.get().getExpirationDate().isBefore(LocalDate.now())) {
            throw new InvalidOtpException("Promo is expired");
        }

        Order order = createOrder(userId);
        CreateOrderDetailResponse orderDetail = orderDetailService
                .createOrderDetail(orderRequest.getCourses(), order, orderRequest.getPromoId());

        if (orderRequest.getTotal().compareTo(orderDetail.getPriceWithDiscount()) != 0) {
            orderRepository.delete(order);
            throw new DataNotValidException("Backend and front end price calculations do not match!");
        }
        updateTotalOrder(order.getId(), orderDetail.getPriceWithDiscount());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle("Payment Successful");
        notificationRequest.setMessage(String.format("Congratulations! You are now a member of Alpha & Omega. Enjoy exploring our app and discovering new knowledge and skills!"));

        emailUtils.sendEmailOrderSuccess(user.getEmail());
        notificationService.pushOrder(notificationRequest, user.getId());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setTotalDiscount(orderDetail.getTotalDiscount());
        orderResponse.setOriginalPrice(orderDetail.getOriginalPrice());
        orderResponse.setPriceWithDiscount(orderDetail.getPriceWithDiscount());
        orderResponse.setOrderedCourses(orderDetail.getCourse());
        orderResponse.setPaymentStatus(order.getStatus());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setUpdatedAt(order.getUpdatedAt());
        log.info("Successfully create order");


        return orderResponse;
    }


    private Order createOrder(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Order order = new Order();
        order.setId("orr".concat(UUID.randomUUID().toString()));
        order.setUser(user);
        order.setMerchant("Alpha & Omega");
        order.setStatus(true);
        order.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
        order.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

        orderRepository.save(order);
        log.info("Successfully create order");

        return order;
    }


    private Boolean updateTotalOrder(String orderId, BigDecimal total) {
        log.info("Update total price in order");
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
        order.setTotal(total);

        orderRepository.save(order);
        log.info("Successfully update order total");
        return true;
    }


}
