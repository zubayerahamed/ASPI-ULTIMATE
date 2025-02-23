package com.zayaanit.aspi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 9, 2023
 */
@Data
@AllArgsConstructor
public class ReloadSectionParams {
	private String key;
	private String value;
}
