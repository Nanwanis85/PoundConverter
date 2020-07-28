package com.currencycalcaulator.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WazirxETHINRWrapper {
	List<WazirxETHINR> markets;
}
