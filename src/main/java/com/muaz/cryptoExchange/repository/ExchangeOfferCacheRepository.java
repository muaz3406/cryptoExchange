package com.muaz.cryptoExchange.repository;

import com.muaz.cryptoExchange.dto.ExchangeOfferResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ExchangeOfferCacheRepository {

    public static final String HASH_KEY = "ExchangeOffer";

    @Autowired
    private RedisTemplate template;

    public ExchangeOfferResponseDto save(ExchangeOfferResponseDto exchangeOfferResponseDto) {
        template.opsForHash().put(HASH_KEY, exchangeOfferResponseDto.getCustomerId(), exchangeOfferResponseDto);
        template.expire(HASH_KEY, 10, TimeUnit.SECONDS);
        return exchangeOfferResponseDto;
    }

    public ExchangeOfferResponseDto findByCustomerId(Long customerId) {
        return (ExchangeOfferResponseDto) template.opsForHash().get(HASH_KEY, customerId);
    }

}
