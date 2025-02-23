package com.zayaanit.aspi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatatableColumnDefination {

	private String data;
	private String name;
	private boolean searchable;
	private boolean orderable;
	private String searchValue;
	private boolean searchRegex;
}
