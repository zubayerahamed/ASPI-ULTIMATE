package com.zayaanit.aspi.service;

import com.zayaanit.aspi.enums.ImportExportColumnType;

public interface GenericImportExportColumns {

	public int getColumnIndex();
	public String getColumnName();
	public String getColumn();
	public ImportExportColumnType getIect();
	public String getColumnDescription();
}
