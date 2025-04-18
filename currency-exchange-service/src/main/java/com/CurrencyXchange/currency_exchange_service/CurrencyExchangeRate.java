package com.CurrencyXchange.currency_exchange_service;

import java.math.BigDecimal;


public class CurrencyExchangeRate {

    private String from;
    private String to;
    private BigDecimal conversionMultiple;
    public CurrencyExchangeRate() {

    }
    public CurrencyExchangeRate(String from, String to, BigDecimal conversionMultiple) {
        this.from = from;
        this.to = to;
        this.conversionMultiple = conversionMultiple;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public void setConversionMultiple(BigDecimal conversionMultiple) {
        this.conversionMultiple = conversionMultiple;
    }

    @Override
    public String toString() {
        return "CurrencyExchangeRate{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", conversionMultiple=" + conversionMultiple +
                '}';
    }
}