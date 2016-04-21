package com.precioustech.euro.fxref.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.precioustech.euro.fxref.entities.EuroRate;
import com.precioustech.euro.fxref.parser.RateSourceParser;

@Component
@Service
public class CacheableEuroExchangeRateService implements EuroExchangeRateService {

	@Value("${ecbLiveRateUrl}")
	private String liveRateSourceUrl;
	@Value("${ecbHistRateUrl}")
	private String historicalRateSourceUrl;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Map<String, Double> currRatesCache = Maps.newHashMap();
	private final Map<EuroRate, Double> histRatesCache = Maps.newHashMap();
	private static final Logger LOG = Logger.getLogger(CacheableEuroExchangeRateService.class);
	@Autowired
	private RateSourceParser parser;

	@PostConstruct
	public void initService() {
		reloadCache();
	}

	// called by scheduler
	@Scheduled(fixedDelayString="${refresh.interval}")
	public void reloadCache() {
		List<EuroRate> currRates = null;
		List<EuroRate> histRates = null;
		LOG.info("Refreshing live Euro cross rates from url ->" + this.liveRateSourceUrl);
		try {
			currRates = this.parser.parse(liveRateSourceUrl);
			LOG.info(String.format("Found %d live rates", currRates.size()));
		} catch (ParseException e) {
			LOG.error("error encountered whilst fetching live rates", e);
		}
		LOG.info("Refreshing historical Euro cross rates from url ->" + this.historicalRateSourceUrl);
		try {
			histRates = this.parser.parse(historicalRateSourceUrl);
			LOG.info(String.format("Found %d historical rates", histRates.size()));
		} catch (ParseException e) {
			LOG.error("error encountered whilst fetching historical rates", e);
		}
		
		Lock writeLock = this.lock.writeLock();
		writeLock.lock();
		try {
			if (currRates != null) {
				this.currRatesCache.clear();
				for (EuroRate rate : currRates) {
					this.currRatesCache.put(rate.getCurrency(), rate.getRate());
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
		Lock readLock = this.lock.readLock();
		try {
			return this.currRatesCache.get(currency);
		} finally {
			readLock.unlock();
		}
	}

	@Override
	public Double getRate(String currency, DateTime referenceDate) {
		EuroRate rateKey = new EuroRate(currency, null, referenceDate);
		Lock readLock = this.lock.readLock();
		readLock.lock();
		try {
			return this.histRatesCache.get(rateKey);
		} finally {
			readLock.unlock();
		}
	}

}
