package com.muaz.cryptoExchange.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class CurrencyInfoCacheRepository {

    public static final String HASH_KEY = "btcCache";

    @Autowired
    private RedisTemplate template;

    public void save(String currencyType, BigDecimal rate) {
        template.opsForHash().put(HASH_KEY, currencyType, rate);
        template.expire(HASH_KEY, 15, TimeUnit.MINUTES);
    }

    public Optional<BigDecimal> findByCurrencyType(String currencyType) {
        return Optional.ofNullable((BigDecimal) template.opsForHash().get(HASH_KEY, currencyType));
    }

}
