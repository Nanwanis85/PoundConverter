package com.currencycalcaulator.model;

import lombok.Data;

@Data
public class PoundConverterOutput {
	 Double poundsToTransfer;
     Double poundRate;
     Double ethToGbpConversionRate;
     Double wazirxEthInrRate;
     Double totalEth;
     Double ethBuyingCommission;
     Double ethWithdrawlCommission;
     Double transferFees;
     Double wazirxSellingEthFees;
     Double totalEthToInr;
     Double finalBnkAmntViaCrypto;
     Double finalBnkAmntViaTrasnsferWise;
     Double profit;
     Double poundRateViaCrypto;
}
