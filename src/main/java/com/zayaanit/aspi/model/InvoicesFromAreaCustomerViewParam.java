package com.zayaanit.aspi.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 3, 2023
 */
@Data
public class InvoicesFromAreaCustomerViewParam {

	private String fromDate;
	private String toDate;
	private Integer area;
	private Integer customer;
	private Integer store;
	private Integer staff;
	private String itemtype;
}
