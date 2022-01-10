package com.muaz.cryptoExchange.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "ExchangeOfferRequestDto", description = "Requested offer model documentation")
public class ExchangeOfferRequestDto implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private BigDecimal value;
    private String fromCurrency;
    private String toCurrency;
    private Long customerId;
}
