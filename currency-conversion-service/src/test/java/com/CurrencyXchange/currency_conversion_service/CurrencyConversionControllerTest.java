package com.CurrencyXchange.currency_conversion_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyConversionControllerTest {

    @Mock
    private CurrencyExchangeProxy currencyExchangeProxy;

    @InjectMocks
    private CurrencyConversionController currencyConversionController;

    private CurrencyConversion mockExchangeValue;

    @BeforeEach
    void setUp() {
        mockExchangeValue = new CurrencyConversion();
        mockExchangeValue.setId(1L);
        mockExchangeValue.setFrom("USD");
        mockExchangeValue.setTo("EUR");
        mockExchangeValue.setConversionMultiple(new BigDecimal(0.85));
    }

    @Test
    void testCalculateCurrencyConversionFeign_Success() {
        // Arrange
        String from = "USD";
        String to = "EUR";
        BigDecimal quantity = new BigDecimal(100);
        BigDecimal expectedTotal = new BigDecimal(85.00);

        when(currencyExchangeProxy.retrieveExchangeValue(from, to))
                .thenReturn(mockExchangeValue);

        CurrencyConversion result = currencyConversionController.calculateCurrencyConversionFeign(from, to, quantity);

        assertNotNull(result);
        assertEquals(from, result.getFrom());
        assertEquals(to, result.getTo());
        assertEquals(quantity, result.getQuantity());
        assertEquals(mockExchangeValue.getConversionMultiple(), result.getConversionMultiple());
        assertEquals(0, result.getTotalCalculatedAmount().compareTo(expectedTotal));
    }

    @Test
    void testCalculateCurrencyConversionFeign_WithZeroQuantity() {
        String from = "USD";
        String to = "EUR";
        BigDecimal quantity = new BigDecimal(0);

        when(currencyExchangeProxy.retrieveExchangeValue(from, to))
                .thenReturn(mockExchangeValue);

        CurrencyConversion result = currencyConversionController.calculateCurrencyConversionFeign(from, to, quantity);

        assertEquals(0, result.getTotalCalculatedAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testCalculateCurrencyConversionFeign_ProxyReturnsNull() {
        String from = "USD";
        String to = "EUR";
        BigDecimal quantity = new BigDecimal(100);

        when(currencyExchangeProxy.retrieveExchangeValue(from, to))
                .thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            currencyConversionController.calculateCurrencyConversionFeign(from, to, quantity);
        });

        assertTrue(exception.getMessage().contains("Failed to retrieve exchange rate"));
    }
}