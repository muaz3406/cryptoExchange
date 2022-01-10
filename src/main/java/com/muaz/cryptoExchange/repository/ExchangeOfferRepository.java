package com.muaz.cryptoExchange.repository;

import com.muaz.cryptoExchange.entity.ExchangeOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeOfferRepository extends JpaRepository<ExchangeOffer, Long> {
}
