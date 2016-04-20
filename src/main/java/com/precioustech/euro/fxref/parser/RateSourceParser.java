package com.precioustech.euro.fxref.parser;

import java.text.ParseException;
import java.util.List;

import com.precioustech.euro.fxref.entities.EuroRate;

public interface RateSourceParser {
	
	List<EuroRate> parse(String uri) throws ParseException;
}
