package com.precioustech.euro.fxref.parser;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;
import com.precioustech.euro.fxref.entities.EuroRate;

public class EcbXmlRateParser implements RateSourceParser {

	private static final String TAG_CUBE = "Cube";
	private static final String ATTR_TIME = "time";
	private static final String ATTR_CCY = "currency";
	private static final String ATTR_RATE = "rate";
	private static final Logger LOG = Logger.getLogger(EcbXmlRateParser.class);

	@Override
	public List<EuroRate> parse(String uri) throws ParseException {
		List<EuroRate> rates = Lists.newArrayList();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			URL url = new URL(uri);
			URLConnection urlConnection = url.openConnection();
			Document ratesDocument = docBuilder.parse(urlConnection.getInputStream());
			ratesDocument.getDocumentElement().normalize();
			NodeList cubeNodes = ratesDocument.getElementsByTagName(TAG_CUBE);
			DateTime refDate = null;
			for (int nodeCt = 0; nodeCt < cubeNodes.getLength(); nodeCt++) {
				Node cubeNode = cubeNodes.item(nodeCt);
				NamedNodeMap attributeMap = cubeNode.getAttributes();
				if (attributeMap.getNamedItem(ATTR_TIME) != null) {
					Attr attrTime = (Attr) attributeMap.getNamedItem(ATTR_TIME);
					String refDateStr = attrTime.getValue();
					LocalDate localDate = LocalDate.parse(refDateStr);
					refDate = new DateTime(localDate.toDate().getTime());
				} else if (attributeMap.getNamedItem(ATTR_CCY) != null) {
					Attr attrCcy = (Attr) attributeMap.getNamedItem(ATTR_CCY);
					Attr attrRate = (Attr) attributeMap.getNamedItem(ATTR_RATE);
					rates.add(new EuroRate(attrCcy.getValue(), 
							Double.parseDouble(attrRate.getValue()), refDate));
				}

			}

		} catch (Exception e) {
			LOG.error("Error encountered whilst parsing ->" + uri, e);
			throw new ParseException(e.getMessage(), 0);
		}
		return rates;
	}

}
