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
public class WI01Dto {

	private String today;
	private String thisMonth;
	private String thisYear;
}
