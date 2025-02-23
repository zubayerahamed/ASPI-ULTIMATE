package com.zayaanit.aspi.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.service.ImportExportService;

public abstract class AbstractImportExport implements ImportExportService {

	static final String ERROR = "Error is : {}, {}";

	@Autowired
	protected AppConfig appConfig;

	protected static String getFileExtension(File file) {
		String fileName = file.getName();
		int lastDotIndex = fileName.lastIndexOf('.');

		if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
			return fileName.substring(lastDotIndex + 1);
		} else {
			return ""; // No extension found
		}
	}
}
