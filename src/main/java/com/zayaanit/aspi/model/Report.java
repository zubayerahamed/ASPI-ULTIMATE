package com.zayaanit.aspi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

	private String prefix;
	private String code;
	private String title;
}
