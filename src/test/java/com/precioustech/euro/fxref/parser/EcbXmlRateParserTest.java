package com.precioustech.euro.fxref.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;

import com.precioustech.euro.fxref.entities.EuroRate;

public class EcbXmlRateParserTest {
	
	private static final double precision=0.001;

	@Test
	public void singleCubeTest() throws Exception {
		String filePath = "src/test/resources/ecbSingleCube.xml";
		URL fileUrl = new File(filePath).toURI().toURL();
		EcbXmlRateParser xmlRateParser = new EcbXmlRateParser();
		List<EuroRate> rates = xmlRateParser.parse(fileUrl.toExternalForm());
		assertEquals(31, rates.size());
		
		EuroRate usd = rates.get(0);
		assertEquals("USD", usd.getCurrency());
		assertEquals(1.1379, usd.getRate(),precision);
		
		EuroRate zar = rates.get(rates.size()-1);
		assertEquals("ZAR", zar.getCurrency());
		assertEquals(16.1623, zar.getRate(),precision);
	}
	
	@Test
	public void multiCubesTest() throws Exception {
		String filePath = "src/test/resources/ecbMultCubes.xml";
		URL fileUrl = new File(filePath).toURI().toURL();
		EcbXmlRateParser xmlRateParser = new EcbXmlRateParser();
		List<EuroRate> rates = xmlRateParser.parse(fileUrl.toExternalForm());
		assertEquals(93, rates.size());
		
		EuroRate usd1 = rates.get(0);
		assertEquals("USD", usd1.getCurrency());
		assertEquals(1.1379, usd1.getRate(),precision);
		assertEquals(new LocalDate("2016-04-20").toDate().getTime(), usd1.getReferenceDate().getMillis());
		
		EuroRate usd2 = rates.get(31);
		assertEquals("USD", usd2.getCurrency());
		assertEquals(1.1343, usd2.getRate(),precision);
		assertEquals(new LocalDate("2016-04-19").toDate().getTime(), usd2.getReferenceDate().getMillis());
	}
	
	@Test
	public void missingAttr() throws Exception {
		String filePath = "src/test/resources/missingStuff.xml";
		URL fileUrl = new File(filePath).toURI().toURL();
		EcbXmlRateParser xmlRateParser = new EcbXmlRateParser();
		List<EuroRate> rates = xmlRateParser.parse(fileUrl.toExternalForm());
		assertTrue(rates.isEmpty());
	}
}
