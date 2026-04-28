package com.zayaanit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Jul 22, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WF02ReqParam {

	private Integer xbuid;
	private String businessUnitName;
	private Integer xacc;
	private String accountName;
	private int last;
	private String type; 
}
