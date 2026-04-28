package com.zayaanit.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Jul 22, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Builder
public class WS03Dto {

	private String xdate;
	private BigDecimal amount;
	
	public WS03Dto(String xdate, BigDecimal amount) {
		this.xdate = xdate;
		this.amount = amount;
	}
}
