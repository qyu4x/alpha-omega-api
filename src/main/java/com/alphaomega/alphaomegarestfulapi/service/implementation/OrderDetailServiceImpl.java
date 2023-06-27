package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.Course;
import com.alphaomega.alphaomegarestfulapi.entity.Order;
import com.alphaomega.alphaomegarestfulapi.entity.OrderDetail;
import com.alphaomega.alphaomegarestfulapi.entity.Promo;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.request.OrderDetailRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CreateOrderDetailResponse;
import com.alphaomega.alphaomegarestfulapi.payload.response.OrderDetailResponse;
import com.alphaomega.alphaomegarestfulapi.repository.CourseRepository;
import com.alphaomega.alphaomegarestfulapi.repository.OrderDetailRepository;
import com.alphaomega.alphaomegarestfulapi.repository.PromoRepository;
import com.alphaomega.alphaomegarestfulapi.service.OrderDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    private PromoRepository promoRepository;

    private OrderDetailRepository orderDetailRepository;

    private CourseRepository courseRepository;

    public OrderDetailServiceImpl(PromoRepository promoRepository, OrderDetailRepository orderDetailRepository, CourseRepository courseRepository) {
        this.promoRepository = promoRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public CreateOrderDetailResponse createOrderDetail(List<OrderDetailRequest> orderDetailRequests, Order order, String promoId) {
        Promo dummyPromo = new Promo();
        dummyPromo.setTotalDiscount(BigDecimal.ZERO);

        Promo promo = promoRepository.findById(promoId)
                .orElse(dummyPromo);

        AtomicReference<BigDecimal> originalPrice = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalDiscount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> priceWithDiscount = new AtomicReference<>(BigDecimal.ZERO);


        List<OrderDetail> orderDetails = new ArrayList<>();

        List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();
        orderDetailRequests.stream().forEach(orderDetailRequest -> {
            Course course = courseRepository.findById(orderDetailRequest.getCourseId())
                    .orElseThrow(() -> new DataNotFoundException("Course not found"));

            BigDecimal finalPrice = course.getPrice().compareTo(promo.getTotalDiscount()) > 0 ?  course.getPrice().subtract(promo.getTotalDiscount()) : BigDecimal.ZERO;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId("ord-".concat(UUID.randomUUID().toString()));
            orderDetail.setCourse(course);
            orderDetail.setCoursePrice(finalPrice);
            orderDetail.setOrder(order);
            orderDetail.setCreatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());
            orderDetail.setUpdatedAt(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(7)).toLocalDateTime());

            orderDetails.add(orderDetail);

            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setCourseName(course.getTitle());
            orderDetailResponse.setPrice(course.getPrice());
            orderDetailResponses.add(orderDetailResponse);

            priceWithDiscount.getAndUpdate(price -> price.add(finalPrice));
            originalPrice.getAndUpdate(price -> price.add(course.getPrice()));
            totalDiscount.getAndUpdate(price -> price.add(promo.getTotalDiscount()));
        });
        orderDetailRepository.saveAll(orderDetails);
        log.info("Successfully save all order detail for order id {}", order.getId());

        CreateOrderDetailResponse  createOrderDetailResponse = new CreateOrderDetailResponse();
        createOrderDetailResponse.setCourse(orderDetailResponses);
        createOrderDetailResponse.setOriginalPrice(originalPrice.get());
        createOrderDetailResponse.setTotalDiscount(totalDiscount.get());
        createOrderDetailResponse.setPriceWithDiscount(priceWithDiscount.get());

        return createOrderDetailResponse;
    }
}
