package com.zayaanit.aspi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusInfo {

	private String statusCode;
	private String errordescription;
	private String clienttransid;
	private String serverReferenceCode;
	private String cli;
	private String isAvailable;
	private String availablebalance;
	private List<String> deliverystatus;
}
