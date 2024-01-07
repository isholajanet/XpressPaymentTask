package com.project.XpressPayments.controller;


import com.project.XpressPayments.dto.PaymentRequest;
import com.project.XpressPayments.dto.PaymentResponse;
import com.project.XpressPayments.service.PaymentServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private final PaymentServiceImpl paymentService;


    @PostMapping("/airtime")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> buyAirtime(@RequestBody PaymentRequest request) {
        try {

            ResponseEntity<PaymentResponse> response = paymentService.makePayment(request);

            log.info("Response: " + response.getBody());
            return new ResponseEntity<>(response.getBody(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
