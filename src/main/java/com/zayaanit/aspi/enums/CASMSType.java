package com.zayaanit.aspi.enums;

/**
 * @author Zubayer Ahamed
 * @since Dec 5, 2023
 */
public enum CASMSType {

	SALES_INVOICE("Sales Invoice"),
	SALES_RETURN("Sales Return"),
	CUSTOMER_ADJUSTMENT("Customer Adjustment"),
	INVOICE_CURRECTION("Invoice Currection"),
	MONEY_RECEIPT("Money Receipt");

	private String prompt;

	public String getPrompt() {
		return prompt;
	}

	private CASMSType(String prompt) {
		this.prompt = prompt;
	}
}
