package com.CurrencyXchange.currency_conversion_service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyConversionController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    @CircuitBreaker(name = "currency-exchange", fallbackMethod = "fallbackCalculateCurrencyConversion")
    @Retry(name = "currency-exchange")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        logger.info("Converting {} {} to {}", quantity, from, to);

        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be a positive number");
        }

        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        if (currencyConversion == null) {
            logger.error("Failed to retrieve exchange rate from {} to {}", from, to);
            throw new RuntimeException("Failed to retrieve exchange rate");
        }

        logger.info("Exchange rate retrieved: 1 {} = {} {}",
                from, currencyConversion.getConversionMultiple(), to);

        BigDecimal totalCalculatedAmount = quantity
                .multiply(currencyConversion.getConversionMultiple())
                .setScale(2, RoundingMode.HALF_UP);

        return new CurrencyConversion(
                currencyConversion.getId(),
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                totalCalculatedAmount);
    }


    public CurrencyConversion fallbackCalculateCurrencyConversion(
            String from,
            String to,
            BigDecimal quantity,
            Exception ex) {

        logger.warn("Fallback method executed due to: {}", ex.toString());


        BigDecimal defaultConversionRate = getDefaultConversionRate(from, to);

        BigDecimal totalCalculatedAmount = quantity
                .multiply(defaultConversionRate)
                .setScale(2, RoundingMode.HALF_UP);

        return new CurrencyConversion(
                999L, // fallback ID
                from,
                to,
                quantity,
                defaultConversionRate,
                totalCalculatedAmount);
    }

    //maybe this method should be somewhere else
    private BigDecimal getDefaultConversionRate(String from, String to) {
        if (from.equals("USD") && to.equals("AED")) {
            return new BigDecimal("3.6725");
        } else if (from.equals("USD") && to.equals("AFN")) {
            return new BigDecimal("72.40482");
        } else if (from.equals("USD") && to.equals("ALL")) {
            return new BigDecimal("87.37927");
        } else if (from.equals("USD") && to.equals("AMD")) {
            return new BigDecimal("391.388425");
        } else if (from.equals("USD") && to.equals("ANG")) {
            return new BigDecimal("1.79");
        } else if (from.equals("USD") && to.equals("AOA")) {
            return new BigDecimal("919.511078");
        } else if (from.equals("USD") && to.equals("ARS")) {
            return new BigDecimal("1172.83");
        } else if (from.equals("USD") && to.equals("AUD")) {
            return new BigDecimal("1.566967");
        } else if (from.equals("USD") && to.equals("AWG")) {
            return new BigDecimal("1.79");
        } else if (from.equals("USD") && to.equals("AZN")) {
            return new BigDecimal("1.701333");
        } else if (from.equals("USD") && to.equals("BAM")) {
            return new BigDecimal("1.720158");
        } else if (from.equals("USD") && to.equals("BBD")) {
            return new BigDecimal("2");
        } else if (from.equals("USD") && to.equals("BDT")) {
            return new BigDecimal("121.553235");
        } else if (from.equals("USD") && to.equals("BGN")) {
            return new BigDecimal("1.720322");
        } else if (from.equals("USD") && to.equals("BHD")) {
            return new BigDecimal("0.376");
        } else if (from.equals("USD") && to.equals("BIF")) {
            return new BigDecimal("2960.979884");
        } else if (from.equals("USD") && to.equals("BMD")) {
            return new BigDecimal("1");
        } else if (from.equals("USD") && to.equals("BND")) {
            return new BigDecimal("1.311638");
        } else if (from.equals("USD") && to.equals("BOB")) {
            return new BigDecimal("6.925559");
        } else if (from.equals("USD") && to.equals("BRL")) {
            return new BigDecimal("5.870608");
        } else if (from.equals("USD") && to.equals("BSD")) {
            return new BigDecimal("1");
        } else if (from.equals("USD") && to.equals("BTN")) {
            return new BigDecimal("85.456535");
        } else if (from.equals("USD") && to.equals("BWP")) {
            return new BigDecimal("13.796039");
        } else if (from.equals("USD") && to.equals("BYN")) {
            return new BigDecimal("3.089503");
        } else if (from.equals("USD") && to.equals("BZD")) {
            return new BigDecimal("2");
        } else if (from.equals("USD") && to.equals("CAD")) {
            return new BigDecimal("1.385362");
        } else if (from.equals("USD") && to.equals("CDF")) {
            return new BigDecimal("2884.734331");
        } else if (from.equals("USD") && to.equals("CHF")) {
            return new BigDecimal("0.818248");
        } else if (from.equals("USD") && to.equals("CLP")) {
            return new BigDecimal("968.391782");
        } else if (from.equals("USD") && to.equals("CNY")) {
            return new BigDecimal("7.302003");
        } else if (from.equals("USD") && to.equals("COP")) {
            return new BigDecimal("4328.582971");
        } else if (from.equals("USD") && to.equals("CRC")) {
            return new BigDecimal("502.860283");
        } else if (from.equals("USD") && to.equals("CUP")) {
            return new BigDecimal("24");
        } else if (from.equals("USD") && to.equals("CVE")) {
            return new BigDecimal("96.978372");
        } else if (from.equals("USD") && to.equals("CZK")) {
            return new BigDecimal("22.023354");
        } else if (from.equals("USD") && to.equals("DJF")) {
            return new BigDecimal("177.721");
        } else if (from.equals("USD") && to.equals("DKK")) {
            return new BigDecimal("6.562792");
        } else if (from.equals("USD") && to.equals("DOP")) {
            return new BigDecimal("60.261975");
        } else if (from.equals("USD") && to.equals("DZD")) {
            return new BigDecimal("132.639812");
        } else if (from.equals("USD") && to.equals("EGP")) {
            return new BigDecimal("51.120114");
        } else if (from.equals("USD") && to.equals("ERN")) {
            return new BigDecimal("15");
        } else if (from.equals("USD") && to.equals("ETB")) {
            return new BigDecimal("131.981683");
        } else if (from.equals("USD") && to.equals("EUR")) {
            return new BigDecimal("0.879504");
        } else {
            // default fallback rate (1:1)
            return BigDecimal.ONE;
        }

    }
}