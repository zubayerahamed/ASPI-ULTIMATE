package com.zayaanit.aspi.model;

import java.util.List;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Data
public class DatatableResponseHelper<T> {

	private int draw;
	private int recordsTotal;
	private int recordsFiltered;
	private List<T> data;
}
