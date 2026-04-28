package com.zayaanit.model;

import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
@Data
public class SMSRequest {

	private List<String> msisdn; 
	private String message;
}
