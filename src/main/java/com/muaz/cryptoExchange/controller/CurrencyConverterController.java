package com.muaz.cryptoExchange.controller;

import com.muaz.cryptoExchange.dto.CurrencyConverterDto;
import com.muaz.cryptoExchange.dto.ExchangeOfferResponseDto;
import com.muaz.cryptoExchange.exception.BlockChainServiceNotRespondingException;
import com.muaz.cryptoExchange.exception.NoSuchResourceFoundException;
import com.muaz.cryptoExchange.repository.ExchangeOfferCacheRepository;
import com.muaz.cryptoExchange.service.CurrencyConverterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Api(value = "CurrencyConverter Api documentation")
public class CurrencyConverterController {

    @Autowired
    private CurrencyConverterService currencyConverterService;
    @Autowired
    private ExchangeOfferCacheRepository exchangeOfferCacheRepository;

    @ApiOperation(value = "create exchange offer", notes = "If not valid request returns 400")
    @PostMapping("/createExchangeOffer")
    public ExchangeOfferResponseDto createExchangeOffer(@RequestBody CurrencyConverterDto currencyConverterDto) throws BlockChainServiceNotRespondingException {
        return currencyConverterService.createExchangeOffer(currencyConverterDto);
    }

    @ApiOperation(value = "Get btc price for 1 unit")
    @GetMapping("/getRate/{from}")
    public BigDecimal getRate(@PathVariable String from) throws BlockChainServiceNotRespondingException {
        return currencyConverterService.getRate(from);
    }

    @ApiOperation(value = "Buy Exchange offer", notes = "If offer expire or customer id not valid returns 404")
    @PostMapping("/buyExchangeOffer/{customerId}")
    public void buyExchangeOffer(@PathVariable Long customerId) {
        ExchangeOfferResponseDto exchangeOfferResponseDto = exchangeOfferCacheRepository.findByCustomerId(customerId);
        if (exchangeOfferResponseDto != null) {
            currencyConverterService.buyExchangeOffer(exchangeOfferResponseDto);
        } else {
            throw new NoSuchResourceFoundException("Offer expires after 10 seconds");
        }
    }
}
