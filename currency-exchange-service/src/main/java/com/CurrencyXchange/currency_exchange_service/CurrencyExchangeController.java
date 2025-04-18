package com.CurrencyXchange.currency_exchange_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@RestController
public class CurrencyExchangeController {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	private final RestTemplate restTemplate;

	public CurrencyExchangeController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchangeRate getExchangeRate(@PathVariable String from, @PathVariable String to) {
		logger.info("Retrieving exchange rate from {} to {}", from, to);

		from = from.toUpperCase();
		to = to.toUpperCase(Locale.ROOT);

		System.out.println(from + to);

		String url = "https://open.er-api.com/v6/latest/" + from;

		System.out.println(url);


		try {
			ExchangeRatesApiResponse response = restTemplate.getForObject(url, ExchangeRatesApiResponse.class);

			if (response == null) {
				logger.error("Failed to retrieve exchange rates from external API");
				throw new RuntimeException("Failed to retrieve exchange rates from external API");
			}

			logger.debug("API Response: {}", response);

			System.out.println(response.getRates());

			if (response.getRates() == null || !response.getRates().containsKey(to)) {
				logger.error("Currency not found: {}", to);
				throw new IllegalArgumentException("Currency not found: " + to);
			}

			BigDecimal rate = BigDecimal.valueOf(response.getRates().get(to))
					.setScale(6, RoundingMode.HALF_UP);

			logger.info("Exchange rate retrieved successfully: 1 {} = {} {}", from, rate, to);

			return new CurrencyExchangeRate(from, to, rate);
		} catch (Exception e) {
			logger.error("Error retrieving exchange rate", e);
			throw e; // re-throwing it to let global exception handler deal with it
		}
	}
}