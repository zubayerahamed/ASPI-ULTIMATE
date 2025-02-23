package com.zayaanit.aspi.enums;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public enum GPMessageType {

	ENGLISH("English", "1"),
	BANGLA("Bangla", "2"),
	FLASH("Flash", "3");

	private String prompt;
	private String value;

	private GPMessageType(String prompt, String value) {
		this.prompt = prompt;
		this.value = value;
	}

	public String getPrompt() {
		return prompt;
	}

	public String getValue() {
		return value;
	}

}
