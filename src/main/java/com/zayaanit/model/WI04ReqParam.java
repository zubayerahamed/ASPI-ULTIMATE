package com.zayaanit.model;

import java.util.List;

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
public class WI04ReqParam {

	private Integer xbuid;
	private String businessUnitName;
	private Integer xwh;
	private String storeName;
	private List<String> xcodes;
	private String xcategory;
	private Integer xitem;
	private String itemName;
	private Integer xfdays;
	private Integer xtdays;
	private String type; 
}
