package com.muaz.cryptoExchange.service;

import com.muaz.cryptoExchange.dto.CurrencyConverterDto;
import com.muaz.cryptoExchange.dto.ExchangeOfferRequestDto;
import com.muaz.cryptoExchange.dto.ExchangeOfferResponseDto;
import com.muaz.cryptoExchange.entity.ExchangeOffer;
import com.muaz.cryptoExchange.exception.BlockChainServiceNotRespondingException;
import com.muaz.cryptoExchange.repository.CurrencyInfoCacheRepository;
import com.muaz.cryptoExchange.repository.ExchangeOfferCacheRepository;
import com.muaz.cryptoExchange.repository.ExchangeOfferRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConverterServiceTest {

    @InjectMocks
    private CurrencyConverterService currencyConverterService;

    @Mock
    private ExchangeOfferRepository exchangeOfferRepository;
    @Mock
    private ExternalCurrencyService externalCurrencyService;
    @Mock
    private ExchangeOfferCacheRepository exchangeOfferCacheRepository;
    @Mock
    private CurrencyInfoCacheRepository currencyInfoCacheRepository;

    @SneakyThrows
    @Test
    public void shouldCreateExchangeOfferExchangeOffer() {
        CurrencyConverterDto currencyConverterDto = CurrencyConverterDto.builder()
                .customerId(123l)
                .fromCurrency("USD")
                .value(BigDecimal.valueOf(100))
                .build();
        when(externalCurrencyService.getRateFromRest(currencyConverterDto.getFromCurrency())).thenReturn(BigDecimal.TEN);
        currencyConverterService.createExchangeOffer(currencyConverterDto);
    }

    @Test
    public void shouldPersistExchangeOfferAsSuccess() {
        ExchangeOfferResponseDto exchangeOfferResponseDto = ExchangeOfferResponseDto.builder()
                .fromCurrency("USD")
                .customerId(123L)
                .build();
        currencyConverterService.buyExchangeOffer(exchangeOfferResponseDto);
        ArgumentCaptor<ExchangeOffer> exchangeOfferArgumentCaptor = ArgumentCaptor.forClass(ExchangeOffer.class);
        verify(exchangeOfferRepository).save(exchangeOfferArgumentCaptor.capture());
        ExchangeOffer exchangeOffer = exchangeOfferArgumentCaptor.getValue();
        assertEquals("SUCCESS", exchangeOffer.getStatus());
    }

    @Test
    public void shouldGetBtcRateFromApi() throws BlockChainServiceNotRespondingException {
        currencyConverterService.getRate("USD");
        verify(externalCurrencyService).getRateFromRest("USD");
    }
}
