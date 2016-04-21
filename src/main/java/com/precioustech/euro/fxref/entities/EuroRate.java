package com.precioustech.euro.fxref.entities;




import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EuroRate {
	
	private final  String currency;
	private final Double rate;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="YYYY-MM-dd")
	private final DateTime referenceDate;
	private final String strRepresentation;
	
	public EuroRate(String currency, Double rate, DateTime referenceDate ) {
		this.currency=currency;
		this.rate=rate;
		this.referenceDate=referenceDate;
		this.strRepresentation = String.format("1 EUR = %3.5f %s on %s", rate, currency, referenceDate);
	}

	public EuroRate(String currency, Double rate) {
		this(currency,rate, new DateTime(LocalDate.now().toDate().getTime()));
	}
	
	public String getCurrency() {
		return currency;
	}

	public Double getRate() {
		return rate;
	}

	public DateTime getReferenceDate() {
		return referenceDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((referenceDate == null) ? 0 : referenceDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EuroRate other = (EuroRate) obj;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (referenceDate == null) {
			if (other.referenceDate != null)
				return false;
		} else if (!referenceDate.equals(other.referenceDate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return this.strRepresentation;
	}
}
