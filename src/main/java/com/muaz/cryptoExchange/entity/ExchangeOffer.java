package com.muaz.cryptoExchange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeOffer implements Serializable {

    private static final long serialVersionUID = 125464567L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal rate;
    private BigDecimal totalPrice;
    private BigDecimal value;
    private String fromCurrency;
    private String toCurrency;
    @Column(name = "created_on")
    @CreationTimestamp
    private Date createdOn;
    private String status;
    private String errMsg;
    private Long customerId;
}
