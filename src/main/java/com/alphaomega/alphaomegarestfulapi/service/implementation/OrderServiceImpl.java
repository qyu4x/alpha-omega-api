package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.*;
import com.alphaomega.alphaomegarestfulapi.exception.DataAlreadyExistsException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotValidException;
import com.alphaomega.alphaomegarestfulapi.exception.InvalidOtpException;
import com.alphaomega.alphaomegarestfulapi.payload.request.NotificationRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OrderDetailRequest;
import com.alphaomega.alphaomegarestfulapi.payload.request.OrderRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CreateOrderDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.OrderDetailResponse;
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
import java.util.ArrayList;
import java.util.List;
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

    private OrderHistoryRepository orderHistoryRepository;
    private final CourseRepository courseRepository;


    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, UserRepository userRepository, OrderDetailService orderDetailService, PromoRepository promoRepository, ShoppingCartRepository shoppingCartRepository, WishListRepository wishListRepository, NotificationService notificationService, EmailUtils emailUtils, OrderHistoryRepository orderHistoryRepository,
                            CourseRepository courseRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.orderDetailService = orderDetailService;
        this.promoRepository = promoRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.wishListRepository = wishListRepository;
        this.notificationService = notificationService;
        this.emailUtils = emailUtils;
        this.orderHistoryRepository = orderHistoryRepository;
        this.courseRepository = courseRepository;
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

        // check order history
        checkOrderAvailable(orderRequest.getCourses(), userId);

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

        // insert into order history
        insertIntoOrderHistory(orderDetail.getCourse(), user);

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

    public void checkOrderAvailable(List<OrderDetailRequest> orderDetailRequests, String userId) {
        log.info("Check available course in course history");
        orderDetailRequests.stream().forEach(orderDetailRequest -> {
           if (!orderHistoryRepository.findOrderHistoryByUserIdAndCourseId(userId, orderDetailRequest.getCourseId()).isEmpty()) {
               throw new DataAlreadyExistsException("You have already purchased this course");
           }
        });
    }

    public void insertIntoOrderHistory(List<OrderDetailResponse> orderDetailResponses, User user) {
        log.info("Create order histories for user id {}", user.getId());
        List<OrderHistory> orderHistories = new ArrayList<>();
        orderDetailResponses.stream().forEach(orderDetailResponse -> {
            Course course = courseRepository.findById(orderDetailResponse.getCourseId())
                    .orElseThrow(() -> new DataNotFoundException("Oops, course not found"));

            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setId("ods".concat(UUID.randomUUID().toString()));
            orderHistory.setUser(user);
            orderHistory.setCourse(course);
            orderHistory.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            orderHistory.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            orderHistories.add(orderHistory);
        });

        orderHistoryRepository.saveAll(orderHistories);
        log.info("Successfully insert order history");
    }
}
