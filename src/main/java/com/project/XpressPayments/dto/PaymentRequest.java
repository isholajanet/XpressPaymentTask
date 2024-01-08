package com.project.XpressPayments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("uniqueCode")
    private String uniqueCode;
    @JsonProperty("details")
    private UserDetail details;


}
