package com.precioustech.euro.fxref.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.precioustech.euro.fxref.ResourceNotFoundException;
import com.precioustech.euro.fxref.entities.EuroRate;
import com.precioustech.euro.fxref.service.EuroExchangeRateService;

@RestController
@RequestMapping("/v1/euroxrate")
public class EuroExchangeRateController {
	@Autowired
	private EuroExchangeRateService exchangeRateService;
	
	@RequestMapping(value="/live/{currency}",produces="application/json", 
			method=RequestMethod.GET)
	public EuroRate getLiveRate(@PathVariable("currency") String currency) {
		Double rate = this.exchangeRateService.getRate(currency);
		if(rate == null) {
			throw new ResourceNotFoundException(String.format("Currency %s not found", currency));
		} else {
			return new EuroRate(currency, rate);
		}
	}
}
