package com.muaz.cryptoExchange.controller;

import com.muaz.cryptoExchange.dto.CurrencyConverterDto;
import com.muaz.cryptoExchange.dto.ExchangeOfferResponseDto;
import com.muaz.cryptoExchange.entity.ExchangeOffer;
import com.muaz.cryptoExchange.exception.BlockChainServiceNotRespondingException;
import com.muaz.cryptoExchange.exception.NoSuchResourceFoundException;
import com.muaz.cryptoExchange.repository.ExchangeOfferCacheRepository;
import com.muaz.cryptoExchange.repository.ExchangeOfferRepository;
import com.muaz.cryptoExchange.service.CurrencyConverterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private ExchangeOfferRepository exchangeOfferRepository;

    @ApiOperation(value = "create exchange offer", notes = "If not valid request returns 400")
    @PostMapping("/createExchangeOffer")
    public ResponseEntity<ExchangeOfferResponseDto> createExchangeOffer(@RequestBody CurrencyConverterDto currencyConverterDto) throws BlockChainServiceNotRespondingException {
        ExchangeOfferResponseDto exchangeOfferResponseDto = currencyConverterService.createExchangeOffer(currencyConverterDto);
        return new ResponseEntity<>(exchangeOfferResponseDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get btc price for 1 unit")
    @GetMapping("/getRate/{from}")
    public ResponseEntity<BigDecimal> getRate(@PathVariable String from) throws BlockChainServiceNotRespondingException {
        return new ResponseEntity<>(currencyConverterService.getRate(from), HttpStatus.OK);
    }

    @ApiOperation(value = "Buy Exchange offer", notes = "If offer expire or customer id not valid returns 404")
    @PostMapping("/buyExchangeOffer/{customerId}")
    public ResponseEntity<ExchangeOffer> buyExchangeOffer(@PathVariable Long customerId) {
        ExchangeOfferResponseDto exchangeOfferResponseDto = exchangeOfferCacheRepository.findByCustomerId(customerId);
        if (exchangeOfferResponseDto != null) {
            ExchangeOffer exchangeOffer = currencyConverterService.buyExchangeOffer(exchangeOfferResponseDto);
            return new ResponseEntity<>(exchangeOffer, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
