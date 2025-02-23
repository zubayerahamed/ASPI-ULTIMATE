package com.zayaanit.aspi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 8, 2023
 */
@Data
public class GPSMSReqBody {

	private String username;
	private String password;
	private String apicode;
	private List<String> msisdn = new ArrayList<>();
	private String countrycode;
	private String cli;
	private String messagetype;
	private String message;
	private String messageid;
}
