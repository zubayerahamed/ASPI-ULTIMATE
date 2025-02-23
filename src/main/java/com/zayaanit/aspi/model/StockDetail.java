package com.zayaanit.aspi.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 21, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDetail {

	private Integer itemCode;
	private String itemName;
	private BigDecimal reqQty;
	private BigDecimal availableQty;
	private BigDecimal deviation;
	private Integer fromStoreCode;
	private String fromStoreName;
	private Integer fromBusienssCode;
	private String fromBusinessUnitName;
}
