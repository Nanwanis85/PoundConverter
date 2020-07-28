package com.currencycalcaulator.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WazirxETHINR {
	String baseMarket;
	String quoteMarket;
	Double last;
}
