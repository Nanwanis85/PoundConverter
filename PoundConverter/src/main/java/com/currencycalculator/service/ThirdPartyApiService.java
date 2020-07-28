package com.currencycalculator.service;

import java.util.Optional;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.json.JSONObject;

import com.currencycalcaulator.model.ETHGBP;
import com.currencycalcaulator.model.WazirxETHINR;
import com.currencycalcaulator.model.WazirxETHINRWrapper;

public class ThirdPartyApiService {

	private static final String tickerPriceUrl= "https://api.binance.com/api/v3/ticker/24hr?symbol=ETHGBP";

	private static final String poundToInrUrl= "https://api.exchangeratesapi.io/latest?base=GBP";

	
	private static final String wazirxETHINRUrl= "https://api.wazirx.com/api/v2/market-status";
	
	public ETHGBP callTickerPriceService() throws Exception {
		ClientRequest request = new ClientRequest(tickerPriceUrl);
		ClientResponse<ETHGBP> response = request.get(ETHGBP.class);
		int apiResponseCode = response.getResponseStatus().getStatusCode();
		if(response.getResponseStatus().getStatusCode() != 200){
			throw new RuntimeException("Failed with HTTP error code : " + apiResponseCode);
		}
		ETHGBP ethGBPValue = response.getEntity();
		return ethGBPValue;
	}

	public String getINRValueOfPound() throws Exception{
		ClientRequest request = new ClientRequest(poundToInrUrl);
		ClientResponse<String> response = request.get(String.class);
		int apiResponseCode = response.getResponseStatus().getStatusCode();
		if(response.getResponseStatus().getStatusCode() != 200){
			throw new RuntimeException("Failed with HTTP error code : " + apiResponseCode);
		}
		response.getEntity();
		JSONObject jsonObject = new JSONObject(response.getEntity());
		JSONObject obj = (JSONObject)jsonObject.get("rates");
		return obj.get("INR").toString();
	}
	
	public WazirxETHINR getWazirxETHINRRate() throws Exception {
		ClientRequest request = new ClientRequest(wazirxETHINRUrl);
		ClientResponse<WazirxETHINRWrapper> response = request.get(WazirxETHINRWrapper.class);
		int apiResponseCode = response.getResponseStatus().getStatusCode();
		if(response.getResponseStatus().getStatusCode() != 200){
			throw new RuntimeException("Failed with HTTP error code : " + apiResponseCode);
		}
		WazirxETHINRWrapper wazirxWrapper = response.getEntity();
		WazirxETHINR wazirxEthInrValue = new WazirxETHINR();
		Optional<WazirxETHINR> wazirxResponse = wazirxWrapper.getMarkets().stream().filter(x->x.getBaseMarket().equals("eth")&&x.getQuoteMarket().equals("inr")).findFirst();
		if(wazirxResponse.isPresent()) {
			wazirxEthInrValue = wazirxResponse.get();
		}
		return wazirxEthInrValue;
	}
}
