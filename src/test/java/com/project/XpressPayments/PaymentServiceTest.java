package com.project.XpressPayments;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.XpressPayments.dto.PaymentRequest;
import com.project.XpressPayments.dto.PaymentResponse;
import com.project.XpressPayments.dto.UserDetail;
import com.project.XpressPayments.service.PaymentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.project.XpressPayments.service.PaymentServiceImpl.calculateHMAC512;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PaymentServiceImpl.class, Hex.class})
@TestPropertySource(locations = "classpath:application-test.yml")
public class PaymentServiceTest {

    @Value("${payment.api.url}")
    private String url;

    @Value("${payment.private.key}")
    private String privateKey;

    @Value("${payment.public.key}")
    private String publicKey;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;


    public void setUp(){
        restTemplate = new RestTemplate();
        paymentService = new PaymentServiceImpl(url, publicKey,privateKey, restTemplate);

    }

    @Test
    public void makePayment_validPhoneNumber_successfulPayment() throws JsonProcessingException {

        UserDetail details = UserDetail.builder()
                .phoneNumber("08033333333")
                .amount(100)
                .build();

        PaymentRequest request = PaymentRequest.builder().requestId("1245")
                .uniqueCode("MTN_4134")
                .details(details)
                .build();

        String expectedPaymentHash = calculateHMAC512(request);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(PaymentResponse.class)))
                .thenReturn(ResponseEntity.ok(new PaymentResponse()));

        ResponseEntity<PaymentResponse> responseEntity = paymentService.makePayment(request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPaymentHash, calculateHMAC512(request));

    }

    @Test
    public void makePayment_invalidPhoneNumber_badRequest() {
        UserDetail details = UserDetail.builder()
                .phoneNumber("08033333333")
                .amount(100)
                .build();

        PaymentRequest request = PaymentRequest.builder().requestId("1245")
                .uniqueCode("MTN_4134")
                .details(details)
                .build();

        ResponseEntity<PaymentResponse> responseEntity = paymentService.makePayment(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void calculateHMAC512_validRequest_correctHash() throws JsonProcessingException {
        UserDetail details = UserDetail.builder()
                .phoneNumber("08033333333")
                .amount(100)
                .build();

        PaymentRequest request = PaymentRequest.builder().requestId("1245")
                .uniqueCode("MTN_4134")
                .details(details)
                .build();


        String expectedHash = calculateHMAC512(request);

        String actualHash = calculateHMAC512(request);


        assertEquals(expectedHash, actualHash);
    }

    private String calculateHMAC512(PaymentRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writeValueAsString(request);

        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(privateKey.getBytes(), HMAC_SHA512);
        Mac mac = null;

        try {
            mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return String.valueOf(Hex.encode(mac.doFinal(data.getBytes())));

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }




}
