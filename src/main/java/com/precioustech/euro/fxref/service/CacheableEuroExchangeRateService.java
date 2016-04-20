package com.precioustech.euro.fxref.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.precioustech.euro.fxref.entities.EuroRate;
import com.precioustech.euro.fxref.parser.RateSourceParser;

public class CacheableEuroExchangeRateService implements EuroExchangeRateService {

	private final String liveRateSourceUrl;
	private final String historicalRateSourceUrl;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Map<EuroRate, Double> currRatesCache = Maps.newHashMap();
	private final Map<EuroRate, Double> histRatesCache = Maps.newHashMap();
	private static final Logger LOG = Logger.getLogger(CacheableEuroExchangeRateService.class);
	@Autowired
	private RateSourceParser parser;

	public CacheableEuroExchangeRateService(final String liveRateSourceUrl, final String historicalRateSourceUrl) {
		this.liveRateSourceUrl = liveRateSourceUrl;
		this.historicalRateSourceUrl = historicalRateSourceUrl;
	}

	// called by scheduler
	public void reloadCache() {
		List<EuroRate> currRates = null;
		List<EuroRate> histRates = null;
		try {
			currRates = this.parser.parse(liveRateSourceUrl);

		} catch (ParseException e) {
			LOG.error("error encountered whilst fetching live rates", e);
		}
		try {
			histRates = this.parser.parse(historicalRateSourceUrl);

		} catch (ParseException e) {
			LOG.error("error encountered whilst fetching historical rates", e);
		}
		
		Lock writeLock = this.lock.writeLock();
		try {
			if (currRates != null) {
				this.currRatesCache.clear();
				for (EuroRate rate : currRates) {
					this.currRatesCache.put(rate, rate.getRate());
				}
			}
			if (histRates != null) {
				this.histRatesCache.clear();
				for (EuroRate rate : histRates) {
					this.histRatesCache.put(rate, rate.getRate());
				}
			}
		} finally {
			writeLock.unlock();
		}
	}

	@Override
	public Double getRate(String currency) {
		EuroRate rateKey = new EuroRate(currency, null);
		Lock readLock = this.lock.readLock();
		try {
			return this.currRatesCache.get(rateKey);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Double getRate(String currency, DateTime referenceDate) {
		EuroRate rateKey = new EuroRate(currency, null, referenceDate);
		Lock readLock = this.lock.readLock();
		try {
			return this.histRatesCache.get(rateKey);
		} finally {
			readLock.unlock();
		}
	}

}
