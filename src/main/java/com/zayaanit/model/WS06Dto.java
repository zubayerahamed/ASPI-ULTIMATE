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
public class WS06Dto {

	private String item;
	private BigDecimal quantity;
	private BigDecimal amount;
	
	public WS06Dto(String item, BigDecimal quantity, BigDecimal amount) {
		this.item = item;
		this.quantity = quantity;
		this.amount = amount;
	}
}
