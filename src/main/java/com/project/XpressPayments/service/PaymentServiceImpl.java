package com.project.XpressPayments.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.XpressPayments.dto.PaymentRequest;
import com.project.XpressPayments.dto.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Slf4j
public class PaymentServiceImpl {

    private final String url;

    private final String privateKey;


    private final String publicKey;

    private final RestTemplate restTemplate;

    public PaymentServiceImpl(@Value("${payment.api.url}") String url,
                              @Value("${payment.private.key}") String privateKey,
                              @Value("${payment.public.key}") String publicKey,
                              RestTemplate restTemplate) {
        this.url = url;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<PaymentResponse> makePayment(PaymentRequest request){

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(request);

            String paymentHash = calculateHMAC512(requestJson, privateKey);

            return makePaymentRequest(request, paymentHash);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    public static String calculateHMAC512(String data, String key) {
        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
        Mac mac = null;

        try {
            mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return String.valueOf(Hex.encode(mac.doFinal(data.getBytes())));

        } catch (NoSuchAlgorithmException  | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    public ResponseEntity<PaymentResponse> makePaymentRequest(PaymentRequest requestBody, String paymentHash ){

        try{

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + publicKey);
            headers.set("PaymentHash", paymentHash);
            headers.set("channel", "API");

            log.info("payment hash: " + paymentHash);

            HttpEntity<PaymentRequest> request = new HttpEntity<>(requestBody, headers);


            ResponseEntity<PaymentResponse> responseEntity = restTemplate.postForEntity(url, request, PaymentResponse.class);
            PaymentResponse response = responseEntity.getBody();


            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Payment successful. Response: " + response);
                return ResponseEntity.ok(response);
            } else {
                log.info("Payment failed. Response: " + responseEntity.getBody());
                return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info("HTTP error. Response: " + e.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        }


    }

}
