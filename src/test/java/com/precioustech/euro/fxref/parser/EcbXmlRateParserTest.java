package com.precioustech.euro.fxref.parser;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import com.precioustech.euro.fxref.entities.EuroRate;

public class EcbXmlRateParserTest {

	@Test
	public void singleCubeTest() throws Exception {
		String filePath = "src/test/resources/ecbSingleCube.xml";
		URL fileUrl = new File(filePath).toURI().toURL();
		EcbXmlRateParser xmlRateParser = new EcbXmlRateParser();
		List<EuroRate> rates = xmlRateParser.parse(fileUrl.toExternalForm());
		assertEquals(31, rates.size());
	}
	
	@Test
	public void multiCubesTest() throws Exception {
		String filePath = "src/test/resources/ecbMultCubes.xml";
		URL fileUrl = new File(filePath).toURI().toURL();
		EcbXmlRateParser xmlRateParser = new EcbXmlRateParser();
		List<EuroRate> rates = xmlRateParser.parse(fileUrl.toExternalForm());
		assertEquals(93, rates.size());
	}
}
