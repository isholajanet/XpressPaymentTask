package com.project.XpressPayments.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("amount")
    private int amount;


}
