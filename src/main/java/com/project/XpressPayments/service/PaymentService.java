package com.project.XpressPayments.service;

import com.project.XpressPayments.dto.PaymentRequest;
import com.project.XpressPayments.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public interface PaymentService {

    ResponseEntity<PaymentResponse> makePayment(PaymentRequest request);
    ResponseEntity<PaymentResponse> buyAirtime(PaymentRequest request, String paymentHash);
}
