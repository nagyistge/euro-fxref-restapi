package com.precioustech.euro.fxref.controllers;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.precioustech.euro.fxref.MissingParamException;
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
			throw new ResourceNotFoundException(String.format("No live rate found for Currency %s", currency));
		} else {
			return new EuroRate(currency, rate);
		}
	}
	@RequestMapping(value="/historic/{currency}",produces="application/json", 
			method=RequestMethod.GET)
	public EuroRate getHistoricRate(@PathVariable("currency") String currency,
			@RequestParam("refDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
		if(date == null) {
			throw new MissingParamException("Missing mandatory param -> refDate");
		}
		DateTime referenceDate = new DateTime(date.getTime());
		Double rate = this.exchangeRateService.getRate(currency, referenceDate);
		if(rate==null) {
			throw new ResourceNotFoundException(String.format("No historic rate found for Currency %s and date=%s ", 
					currency,referenceDate));
		} else {
			return new EuroRate(currency, rate, referenceDate);
		}
	}
}
