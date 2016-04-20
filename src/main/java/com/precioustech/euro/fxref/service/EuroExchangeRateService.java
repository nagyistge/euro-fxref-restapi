package com.precioustech.euro.fxref.service;

import org.joda.time.DateTime;

public interface EuroExchangeRateService {
	
	Double getRate(String currency);
	
	Double getRate(String currency, DateTime referenceDate);
}
