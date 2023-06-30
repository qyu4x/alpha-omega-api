package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.entity.PaymentMethod;
import com.alphaomega.alphaomegarestfulapi.exception.DataNotFoundException;
import com.alphaomega.alphaomegarestfulapi.payload.response.PaymentMethodResponse;
import com.alphaomega.alphaomegarestfulapi.repository.PaymentMethodRepository;
import com.alphaomega.alphaomegarestfulapi.service.PaymentMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    @Override
    @Transactional
    public List<PaymentMethodResponse> findAll() {
        log.info("Find all payment method");
        List<PaymentMethodResponse> paymentMethodResponses = new ArrayList<>();
        paymentMethodRepository.findAll().stream().forEach(paymentMethod -> {
            PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse();
            paymentMethodResponse.setId(paymentMethod.getId());
            paymentMethodResponse.setName(paymentMethod.getName());
            paymentMethodResponse.setLogoUrl(paymentMethod.getLogoUrl());
            paymentMethodResponse.setCreatedAt(paymentMethod.getCreatedAt());

            paymentMethodResponses.add(paymentMethodResponse);
        });
        log.info("Successfully find all payment method");
        return paymentMethodResponses;
    }

    @Override
    public PaymentMethodResponse findById(String id) {
        log.info("Find payment method with id {}", id);
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Payment method not found"));
        PaymentMethodResponse paymentMethodResponse = new PaymentMethodResponse();
        paymentMethodResponse.setId(paymentMethod.getId());
        paymentMethodResponse.setName(paymentMethod.getName());
        paymentMethodResponse.setLogoUrl(paymentMethod.getLogoUrl());
        paymentMethodResponse.setCreatedAt(paymentMethod.getCreatedAt());

        return paymentMethodResponse;
    }
}
