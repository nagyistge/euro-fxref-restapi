package com.precioustech.euro.fxref.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.precioustech.euro.fxref.controllers.EuroExchangeRateController;
import com.precioustech.euro.fxref.parser.RateSourceParser;
import com.precioustech.euro.fxref.service.EuroExchangeRateService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = EuroExchangeRateControllerTest.TestConfig.class)
@WebAppConfiguration
public class EuroExchangeRateControllerTest {
	private MockMvc  mockMvc;
	
//	@Autowired
//	private WebApplicationContext context;
	
	@Autowired
	private EuroExchangeRateController controller;
	
	@Test
	public void simpleLiveRate() throws Exception {
		String localDate = LocalDate.now().toString();
		String expected = "{\"currency\":\"CHF\",\"rate\":1.0912,\"referenceDate\":\""+localDate+"\"}";
		mockMvc.perform(get("/v1/euroxrate/live/CHF"))
		.andExpect(status().isOk()).andExpect(content().string(expected));
		mockMvc.perform(get("/v1/euroxrate/live/FOO"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void historicRate() throws Exception {
		String expected = "{\"currency\":\"CHF\",\"rate\":1.0956,\"referenceDate\":\"2016-04-14\"}";
		mockMvc.perform(get("/v1/euroxrate/historic/CHF?refDate=2016-04-14"))
		.andExpect(status().isOk()).andExpect(content().string(expected));
		mockMvc.perform(get("/v1/euroxrate/historic/CHF"))
		.andExpect(status().is4xxClientError());
		mockMvc.perform(get("/v1/euroxrate/historic/CHF?refDate=2011-04-14"))
		.andExpect(status().isNotFound());
	}
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
				.build();
	}
	
	@Configuration
	public static class TestConfig {
		
		@Bean
		public RateSourceParser parser() throws ParseException {
			RateSourceParser mockParser = mock(RateSourceParser.class);
			return mockParser;
		}
		
		@Bean
		public EuroExchangeRateController controller() {
			return new EuroExchangeRateController();
		}
		
		@Bean
		public EuroExchangeRateService exchangeRateService() {
			EuroExchangeRateService exchangeRateService = mock(EuroExchangeRateService.class);
			when(exchangeRateService.getRate(Matchers.eq("CHF"))).thenReturn(1.0912);
			when(exchangeRateService.getRate(Matchers.eq("FOO"))).thenReturn(null);
			DateTime refDate = new DateTime(new LocalDate("2016-04-14").toDate().getTime());
			when(exchangeRateService.getRate(Matchers.eq("CHF"), Matchers.eq(refDate))).thenReturn(1.0956);
			
			refDate = new DateTime(new LocalDate("2011-04-14").toDate().getTime());
			when(exchangeRateService.getRate(Matchers.eq("CHF"), Matchers.eq(refDate))).thenReturn(null);
			return exchangeRateService;
		}
	}
}
