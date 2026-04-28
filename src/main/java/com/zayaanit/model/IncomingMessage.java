package com.zayaanit.model;

import java.util.ArrayList;
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
public class IncomingMessage {

	private String clientName;
	private List<String> services = new ArrayList<String>();
	private List<String> needToStopServices = new ArrayList<String>();
}
