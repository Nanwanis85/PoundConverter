package com.currencycalculator.service;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.currencycalcaulator.model.ETHGBP;
import com.currencycalcaulator.model.PoundConverterOutput;

public class UiService {
	
	//Ui Service Variables
	static JFrame inputFrame;
	static JFrame outputFrame;
	static JLabel poundsToTransfer;
	static JLabel poundRate;
	static JLabel ethToGbpConversionRate;
	static JLabel wazirxEthInrRate;
	static JLabel totalEth;
	static JLabel ethBuyingCommission;
	static JLabel ethWithdrawlCommission;
	static JLabel transferFees;
	static JLabel wazirxSellingEthFees;
	static JLabel totalEthToInr;
	static JLabel finalBnkAmntViaCrypto;
	static JLabel finalBnkAmntViaTrasnsferWise;
	static JLabel profit;
	static JLabel poundRateViaCrypto;
	JTable resultTable;
	JPanel panel;
	JFrame outPutframe;

	// add Column to the table
	String header[] = new String[] { "Pound Converter Attributes", "Pound Converter Values"};
	
	public void createFrame() {
		//Creating Input Frame
		inputFrame = new JFrame("Pound Converter Input");
		JLabel poundsToTransfer = new JLabel("Enter Pounds To Transfer", 10); 
		panel = new JPanel();
		inputFrame.getContentPane().setLayout(new FlowLayout());
		final JTextField poundValueToTransfer = new JTextField("",10);
		panel.add(poundsToTransfer);
		panel.add(poundValueToTransfer);
		panel.add(new JScrollPane(resultTable));
		inputFrame.add(panel);
		JButton submitButton = new JButton("Submit");
		final PoundConverterOutput output = new PoundConverterOutput();
		
		//Listener to the Button
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent inputAction) {
				output.setPoundsToTransfer(Double.parseDouble(poundValueToTransfer.getText().toString()));
				try {
					createOutputResult(Double.parseDouble(poundValueToTransfer.getText().toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		submitButton.setBounds(100,100,140, 40);  
		inputFrame.add(panel);
		inputFrame.getContentPane().add(submitButton);
		inputFrame.setSize(350,250); 
		inputFrame.setMaximizedBounds(new Rectangle(300, 200));
	    //  Font font = new Font("Serif", Font.ITALIC, 18);
	     // textPane.setFont(font);
		inputFrame.setVisible(true);
	}
	
	public void createOutputResult(Double poundToTransfer) throws Exception {
		//Creating Frame for Output
		outPutframe = new JFrame();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "POUND CONVERTER OUTPUT", TitledBorder.CENTER, TitledBorder.TOP));
		JTable resultTable = new JTable();
		DefaultTableModel dtm = new DefaultTableModel(0, 0);
		dtm.setColumnIdentifiers(header);
		resultTable.setModel(dtm);
		JScrollPane scrollPane =   new JScrollPane(resultTable);
		scrollPane.setSize(200, 200);
		panel.add(scrollPane);
		outPutframe.add(panel);
		PoundConverterOutput output = new PoundConverterOutput();
		
		//Calling Third Party Service
		ThirdPartyApiService apiService = new ThirdPartyApiService();
		String inrValue =apiService.getINRValueOfPound();
		ETHGBP ethGbp = apiService.callTickerPriceService();
		Double wazirx = apiService.getWazirxETHINRRate().getLast();
		//End of Third Party Service
		
		
		//Populating Output Object Model
		populateOutputModelObj(output, poundToTransfer, wazirx, ethGbp, inrValue);

		//Populating Rows in Table
		populateRowsInDataTable(dtm, output);
		
		outPutframe.setSize(400, 400);
		outPutframe.setVisible(true);

	}

	private void populateOutputModelObj(PoundConverterOutput output, Double poundToTransfer, Double wazirx, ETHGBP ethGbp, String inrValue) {
		output.setWazirxEthInrRate(wazirx);
		output.setPoundsToTransfer(poundToTransfer);
		output.setPoundRate(Double.parseDouble(inrValue));
		output.setEthToGbpConversionRate(ethGbp.getLastPrice());
		output.setTotalEth(poundToTransfer/ethGbp.getLastPrice());
		output.setEthBuyingCommission(0.01*ethGbp.getLastPrice());
		output.setEthWithdrawlCommission(0.00168*(ethGbp.getLastPrice()));
		output.setTransferFees(poundToTransfer/1000*5.25);
		output.setTotalEthToInr((output.getTotalEth()*wazirx)-output.getEthBuyingCommission()-output.getEthWithdrawlCommission());
		output.setWazirxSellingEthFees(0.002*output.getTotalEthToInr());
		output.setFinalBnkAmntViaCrypto(output.getTotalEthToInr()-output.getWazirxSellingEthFees());
		output.setFinalBnkAmntViaTrasnsferWise((poundToTransfer* Double.parseDouble(inrValue))-(output.getTransferFees()*(Double.parseDouble(inrValue))));
		output.setProfit(output.getFinalBnkAmntViaCrypto()-output.getFinalBnkAmntViaTrasnsferWise());
		output.setPoundRateViaCrypto(output.getFinalBnkAmntViaCrypto()/poundToTransfer);

		
	}

	private void populateRowsInDataTable(DefaultTableModel dtm, PoundConverterOutput output) {
		dtm.addRow(new Object[]{"Pounds To Transfer is",output.getPoundsToTransfer()});
		dtm.addRow(new Object[]{"PoundRate is ", output.getPoundRate()});
		dtm.addRow(new Object[]{"EthToGbpConversionRate is", output.getEthToGbpConversionRate()});
		dtm.addRow(new Object[]{"WazirxEthInrRate is",output.getWazirxEthInrRate()});
		dtm.addRow(new Object[]{"TotalEth is",output.getTotalEth()});
		dtm.addRow(new Object[]{"EthBuyingCommission is",output.getEthBuyingCommission()});
		dtm.addRow(new Object[]{"EthWithdrawlCommission is",output.getEthWithdrawlCommission()});
		dtm.addRow(new Object[]{"TransferFees is",output.getTransferFees()});
		dtm.addRow(new Object[]{"WazirxSellingEthFees is",output.getWazirxSellingEthFees()});
		dtm.addRow(new Object[]{"TotalEthToInr is",output.getTotalEthToInr()});
		dtm.addRow(new Object[]{"FinalBnkAmntViaCrypto is",output.getFinalBnkAmntViaCrypto()});
		dtm.addRow(new Object[]{"FinalBnkAmntViaTrasnsferWise is",output.getFinalBnkAmntViaTrasnsferWise()});
		dtm.addRow(new Object[]{"Profit is",output.getProfit()});
		dtm.addRow(new Object[]{"PoundRateViaCrypto is",output.getPoundRateViaCrypto()});
	}
	
}
