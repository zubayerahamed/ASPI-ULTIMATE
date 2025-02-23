package com.zayaanit.aspi.service;

import java.io.IOException;
import java.util.List;

import org.springframework.ui.Model;

import com.zayaanit.aspi.model.AsyncCSVResult;
import com.zayaanit.aspi.model.CSVError;
import com.zayaanit.aspi.model.ImportExportModuleColumn;

/**
 * @author Zubayer Ahamed
 * @since Feb 8, 2025
 */
public interface ImportExportService {

	public String showExportImportPage(Model model);

	public void downloadTemplate(AsyncCSVResult asyncCSVResult) throws IOException;

	public void retreiveData(AsyncCSVResult asyncCSVResult) throws IOException;

	public void processCSV(AsyncCSVResult asyncCSVResult);

	public void processDataFromExcel(AsyncCSVResult asyncCSVResult);

	public void importCSV(AsyncCSVResult asyncCSVResult);

	public void confirmImportData(AsyncCSVResult asyncCSVResult);

	public <E extends Enum<E> & GenericImportExportColumns> String getHeader(Class<E> clazz);

	public String getHeader();

	public void generateErrors(List<CSVError> csvErrors, String column, String reason, int rowNumber);

	public <E extends Enum<E> & GenericImportExportColumns> List<ImportExportModuleColumn> getModuleColumns(Class<E> clazz);
}
