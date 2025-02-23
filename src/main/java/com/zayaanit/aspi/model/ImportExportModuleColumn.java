package com.zayaanit.aspi.model;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Feb 8, 2025
 */
@Data
public class ImportExportModuleColumn {

	private int columnIndex;
	private String columnName;
	private String column;
	private String columnType;
	private String cssClass;
	private String columnDescription;
}
