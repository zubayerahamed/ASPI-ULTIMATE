package com.zayaanit.service;

import com.zayaanit.enums.ImportExportColumnType;

public interface GenericImportExportColumns {

	public int getColumnIndex();
	public String getColumnName();
	public String getColumn();
	public ImportExportColumnType getIect();
	public String getColumnDescription();
}
