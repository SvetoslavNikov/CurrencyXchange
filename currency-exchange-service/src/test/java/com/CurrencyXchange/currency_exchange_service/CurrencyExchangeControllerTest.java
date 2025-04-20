package com.CurrencyXchange.currency_exchange_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeController currencyExchangeController;

    private ExchangeRatesApiResponse mockResponse;

    @BeforeEach
    void setUp() {
        currencyExchangeController = new CurrencyExchangeController(restTemplate);

        mockResponse = new ExchangeRatesApiResponse();
        mockResponse.setResult("success");

        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("EUR", 0.85);
        rates.put("GBP", 0.75);
        mockResponse.setRates(rates);
    }

    @Test
    void testGetExchangeRate_Success() {
        String from = "USD";
        String to = "EUR";
        String url = "https://open.er-api.com/v6/latest/" + from;

        when(restTemplate.getForObject(eq(url), eq(ExchangeRatesApiResponse.class)))
                .thenReturn(mockResponse);

        CurrencyExchangeRate result = currencyExchangeController.getExchangeRate(from, to);

        assertNotNull(result);
        assertEquals(from, result.getFrom());
        assertEquals(to, result.getTo());
        assertEquals(new BigDecimal(0.85).setScale(6, RoundingMode.HALF_UP), result.getConversionMultiple());
    }

    @Test
    void testGetExchangeRate_CurrencyNotFound() {
        String from = "USD";
        String to = "XYZ"; // Non-existent currency
        String url = "https://open.er-api.com/v6/latest/" + from;

        when(restTemplate.getForObject(eq(url), eq(ExchangeRatesApiResponse.class)))
                .thenReturn(mockResponse);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyExchangeController.getExchangeRate(from, to);
        });

        assertTrue(exception.getMessage().contains("Currency not found"));
    }

    @Test
    void testGetExchangeRate_ApiFailure() {
        String from = "USD";
        String to = "EUR";
        String url = "https://open.er-api.com/v6/latest/" + from;

        when(restTemplate.getForObject(eq(url), eq(ExchangeRatesApiResponse.class)))
                .thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            currencyExchangeController.getExchangeRate(from, to);
        });

        assertTrue(exception.getMessage().contains("Failed to retrieve exchange rates"));
    }
}