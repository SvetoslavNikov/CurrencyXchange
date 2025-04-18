package com.CurrencyXchange.currency_exchange_service;

import java.util.Map;

public class ExchangeRatesApiResponse {
		private String result;
		private Map<String, Double> rates;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public Map<String, Double> getRates() {
			return rates;
		}

		public void setRates(Map<String, Double> rates) {
			this.rates = rates;
		}

	@Override
	public String toString() {
		return "ExchangeRatesApiResponse{" +
				"result='" + result + '\'' +
				", rates=" + rates +
				'}';
	}
}
