package com.zayaanit.aspi.service.impl;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.model.AsyncCSVResult;
import com.zayaanit.aspi.service.ImportExportService;

/**
 * @author Zubayer Ahamed
 * @since Feb 8, 2025
 */
@Service
public class AsyncCSVProcessor {

	@Async
	public void processDataFromExcel(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.processDataFromExcel(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void processDataFromCSV(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.processCSV(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void confirmImportData(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.confirmImportData(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void importDataFromCSV(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.importCSV(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void exportData(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) throws IOException {
		importExportService.retreiveData(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}
}
