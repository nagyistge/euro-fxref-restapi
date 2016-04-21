package com.precioustech.euro.fxref.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.precioustech.euro.fxref.entities.EuroRate;
import com.precioustech.euro.fxref.parser.RateSourceParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CacheableEuroExchangeRateServiceTest.TestConfig.class)
public class CacheableEuroExchangeRateServiceTest {

	private final static String liveurl = "http://localhost/liveurl";
	private final static String histurl = "http://localhost/histurl";
	private final static DateTime refDate = new DateTime(new LocalDate("2016-04-14").toDate().getTime());
	@Autowired
	private CacheableEuroExchangeRateService euroExRateService; 
	
	@Before
	public void loadcache() {
		euroExRateService.reloadCache();
	}
	
	@Test
	public void findrates() {
		Double chfratelive = euroExRateService.getRate("CHF");
		assertEquals(1.0987, chfratelive,0.0001);
		Double chfratehist = euroExRateService.getRate("CHF", refDate);
		assertEquals(1.0895, chfratehist,0.0001);
	}
	
	public static class TestConfig {
		
		@Bean
		public RateSourceParser parser() throws ParseException {
			RateSourceParser mockParser = mock(RateSourceParser.class);
			List<EuroRate> rates = Lists.newArrayList(new EuroRate("CHF", 1.0987));
			when(mockParser.parse(liveurl)).thenReturn(rates);
			rates = Lists.newArrayList(new EuroRate("CHF", 1.0895, refDate));
			when(mockParser.parse(histurl)).thenReturn(rates);
			return mockParser;
		}
		
		@Bean
		public EuroExchangeRateService exchangeRateService() {
			CacheableEuroExchangeRateService service = new CacheableEuroExchangeRateService();
			return service;
		}
		@Bean
	    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
	        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
	        Properties properties = new Properties();

	        properties.setProperty("ecbLiveRateUrl", liveurl);
	        properties.setProperty("ecbHistRateUrl", histurl);
	        pspc.setProperties(properties);
	        return pspc;
	    }
	}
}
