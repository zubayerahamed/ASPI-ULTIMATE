package com.zayaanit.aspi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SMSResponse {

	private String statusCode;
	private String message;
}
