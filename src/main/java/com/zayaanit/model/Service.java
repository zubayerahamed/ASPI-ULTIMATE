package com.zayaanit.model;

import com.zayaanit.enums.ServiceStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since May 21, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service {

	private String serviceName;
	private ServiceStatus status;
	private String serviceUpDate;
	private String serviceUpTime;
}
