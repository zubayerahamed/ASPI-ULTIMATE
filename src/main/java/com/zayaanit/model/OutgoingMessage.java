package com.zayaanit.model;

import java.util.Date;
import java.util.List;

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
public class OutgoingMessage {

	private String clientName;
	private List<Service> services;
	private String serverUpTime;
	private String cpuUsages;
	private String memoryUsages;
	private Date currentDateTime;
	private Integer intervalTime;
	private String heighestCPUUsage;
	private String heighestMemoryUsage;
	private String availableDiskSpace;
	private Double availablePercentage;
}
