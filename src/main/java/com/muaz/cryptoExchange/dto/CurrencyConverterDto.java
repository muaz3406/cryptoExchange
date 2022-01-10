package com.muaz.cryptoExchange.dto;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@ApiModel(value = "Requested offer model documentation", description = "Model")
public class CurrencyConverterDto {

    private String fromCurrency;
    private Long customerId;
    private String toCurrency;
    private BigDecimal value;
}
