package com.muaz.cryptoExchange.service;

import com.google.common.collect.Range;
import com.muaz.cryptoExchange.dto.CurrencyConverterDto;
import com.muaz.cryptoExchange.dto.ExchangeOfferResponseDto;
import com.muaz.cryptoExchange.entity.ExchangeOffer;
import com.muaz.cryptoExchange.exception.BadRequestException;
import com.muaz.cryptoExchange.exception.BlockChainServiceNotRespondingException;
import com.muaz.cryptoExchange.repository.CurrencyInfoCacheRepository;
import com.muaz.cryptoExchange.repository.ExchangeOfferCacheRepository;
import com.muaz.cryptoExchange.repository.ExchangeOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConverterService {

    private final ExchangeOfferRepository exchangeOfferRepository;
    private final ExternalCurrencyService externalCurrencyService;
    private final ExchangeOfferCacheRepository exchangeOfferCacheRepository;
    private final CurrencyInfoCacheRepository currencyInfoCacheRepository;

    public BigDecimal getRate(String from) throws BlockChainServiceNotRespondingException {
        log.debug("getRate from:{}", from);
        Optional<BigDecimal> optionalRate = currencyInfoCacheRepository.findByCurrencyType(from);
        if (optionalRate.isPresent()) {
            return optionalRate.get();
        } else {
            return externalCurrencyService.getRateFromRest(from);
        }
    }

    public ExchangeOfferResponseDto createExchangeOffer(CurrencyConverterDto currencyConverterDto) throws BlockChainServiceNotRespondingException {
        log.debug("getExchangeOffer currencyConverterDto: {}", currencyConverterDto);
        checkValidOffer(currencyConverterDto);
        String fromCurrency = currencyConverterDto.getFromCurrency();
        Optional<BigDecimal> optionalRate = currencyInfoCacheRepository.findByCurrencyType(fromCurrency);
        if (optionalRate.isPresent()) {
            return prepareOffer(currencyConverterDto, optionalRate.get());
        }
        return prepareOffer(currencyConverterDto, externalCurrencyService.getRateFromRest(fromCurrency));
    }

    public ExchangeOffer buyExchangeOffer(ExchangeOfferResponseDto exchangeOfferResponseDto) {
        log.debug("buyExchangeOffer exchangeOfferResponseDto: {}", exchangeOfferResponseDto);

        ExchangeOffer exchangeOffer = ExchangeOffer.builder()
                .fromCurrency(exchangeOfferResponseDto.getFromCurrency())
                .rate(exchangeOfferResponseDto.getRate())
                .status("SUCCESS")
                .toCurrency("BTC")
                .totalPrice(exchangeOfferResponseDto.getTotalPrice())
                .customerId(exchangeOfferResponseDto.getCustomerId())
                .value(exchangeOfferResponseDto.getValue())
                .build();
        return exchangeOfferRepository.save(exchangeOffer);
    }

    private void checkValidOffer(CurrencyConverterDto currencyConverterDto) {

        if (!InValidRange(currencyConverterDto.getValue())) {
            throw new BadRequestException("Value can not be under 25");
        } else if (!isValidCurrency(currencyConverterDto.getFromCurrency())) {
            throw new BadRequestException("From currency have to be USD or EUR");
        } else if (currencyConverterDto.getCustomerId() == null) {
            throw new BadRequestException("User not found");
        } else if ("BTC".equals(currencyConverterDto.getToCurrency())) {
            throw new BadRequestException("Only btc");
        }
    }

    private boolean isValidCurrency(String fromCurrency) {
        return "USD".equals(fromCurrency) || "EUR".equals(fromCurrency);
    }

    private boolean InValidRange(BigDecimal value) {
        Range<BigDecimal> myRange = Range.closed(BigDecimal.valueOf(25), BigDecimal.valueOf(5000));
        return myRange.contains(value);
    }

    private ExchangeOfferResponseDto prepareOffer(CurrencyConverterDto currencyConverterDto, BigDecimal rate) {
        ExchangeOfferResponseDto exchangeOfferResponseDto = ExchangeOfferResponseDto.builder()
                .rate(rate)
                .value(currencyConverterDto.getValue())
                .toCurrency("BTC")
                .customerId(currencyConverterDto.getCustomerId())
                .fromCurrency(currencyConverterDto.getFromCurrency())
                .toCurrency(currencyConverterDto.getToCurrency())
                .totalPrice(currencyConverterDto.getValue().multiply(rate))
                .build();
        log.debug("prepareOffer caches with exchangeOfferResponseDto: {}", exchangeOfferResponseDto);
        return exchangeOfferCacheRepository.save(exchangeOfferResponseDto);
    }

}
