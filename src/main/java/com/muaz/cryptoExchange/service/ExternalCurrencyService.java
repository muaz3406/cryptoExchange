package com.muaz.cryptoExchange.service;

import com.muaz.cryptoExchange.exception.BlockChainServiceNotRespondingException;
import com.muaz.cryptoExchange.repository.CurrencyInfoCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@Service
public class ExternalCurrencyService {

    private static final String URI = "https://blockchain.info/tobtc?currency={from}&value={value}";

    private final RestTemplate restTemplate;
    private final CurrencyInfoCacheRepository currencyInfoCacheRepository;

    public ExternalCurrencyService(RestTemplateBuilder restTemplateBuilder, CurrencyInfoCacheRepository currencyInfoCacheRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.currencyInfoCacheRepository = currencyInfoCacheRepository;
    }

    @Retryable(value = Exception.class,
            maxAttempts = 2, backoff = @Backoff(delay = 3000))
    public BigDecimal getRateFromRest(String from) throws BlockChainServiceNotRespondingException {
        try {
            String response = restTemplate.getForObject(URI, String.class, from, BigDecimal.ONE);
            BigDecimal rate = new BigDecimal(response);
            currencyInfoCacheRepository.save(from, rate);
            log.info("getRateFromRest from:{}, to BTC, response:{}", from, response);
            return rate;
        } catch (Exception e) {
            throw new BlockChainServiceNotRespondingException("blockchain.info");
        }
    }

}
