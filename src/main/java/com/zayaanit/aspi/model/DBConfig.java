package com.zayaanit.aspi.model;

import lombok.Data;

@Data
public class DBConfig {

	private String jndiName;
	private String connectionURL;
	private String driverName;
	private String username;
	private String password;
}
