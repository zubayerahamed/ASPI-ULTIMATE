package com.zayaanit.aspi.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.enums.ExcelCellType;
import com.zayaanit.aspi.model.DataDownloadAccount;
import com.zayaanit.aspi.model.DataDownloadAccountsSearchParam;
import com.zayaanit.aspi.service.DataDownloadAccountService;

import jakarta.servlet.ServletOutputStream;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2024
 */
@Service
public class ExcelService {

	private static final int BATCH_SIZE = 100;

	public void generateExcelFileAsync(
			DataDownloadAccountService dataDownloadAccountService,
			DataDownloadAccountsSearchParam param, 
			Integer zid, 
			ServletOutputStream out) throws IOException {

		Workbook workbook = new SXSSFWorkbook(BATCH_SIZE);
		Sheet sheet = workbook.createSheet(param.getXtype());
		Row row = sheet.createRow(0);

		CellStyle textStyle = workbook.createCellStyle();

		CellStyle integerStyle = workbook.createCellStyle();
		integerStyle.setDataFormat(workbook.createDataFormat().getFormat("0"));

		CellStyle dateStyle = workbook.createCellStyle();
		short dateFormat = workbook.createDataFormat().getFormat("yyyy-MM-dd");
		dateStyle.setDataFormat(dateFormat);

		CellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

		createCell(workbook, sheet, row, 0, "Voucher Date", ExcelCellType.TEXT, textStyle);
		createCell(workbook, sheet, row, 1, "Voucher Number", ExcelCellType.INTEGER, integerStyle);
		createCell(workbook, sheet, row, 2, "Voucher Type", ExcelCellType.TEXT, textStyle);
		createCell(workbook, sheet, row, 3, "Debit Ledger", ExcelCellType.TEXT, textStyle);
		createCell(workbook, sheet, row, 4, "Debit Amount", ExcelCellType.DOUBLE, doubleStyle);
		createCell(workbook, sheet, row, 5, "Credit Ledger", ExcelCellType.TEXT, textStyle);
		createCell(workbook, sheet, row, 6, "Credit Amount", ExcelCellType.DOUBLE, doubleStyle);
		createCell(workbook, sheet, row, 7, "Narration", ExcelCellType.RICHTEXT, textStyle);

		out.flush();

		// Add more data as needed
		int totalSize = 0;
		int chunkLimit = BATCH_SIZE;
		int offset = 0;
		int dataSize = 1;
		AtomicInteger rowCount = new AtomicInteger(1);

		while(dataSize > 0) {
			List<DataDownloadAccount> result = dataDownloadAccountService.AD19(chunkLimit, offset, null, DatatableSortOrderType.DESC, "", 0, param, zid);
			dataSize = result.size();

			offset = offset + dataSize;

			constractExcelLines(workbook, sheet, result, rowCount, textStyle, integerStyle, dateStyle, doubleStyle);

			out.flush();

			totalSize = totalSize + dataSize;
			if(dataSize < chunkLimit) {
				break; 
			}
		}

		workbook.write(out);
		out.flush();

		workbook.close();
	}

	private void constractExcelLines(Workbook workbook, Sheet sheet, List<DataDownloadAccount> result, AtomicInteger rowCount, CellStyle textStyle, CellStyle integerStyle, CellStyle dateStyle, CellStyle doubleStyle) {

		for (DataDownloadAccount d : result) {
			Row row = sheet.createRow(rowCount.getAndIncrement());
			int columnCount = 0;

			createCell(workbook, sheet, row, columnCount++, d.getVdate(), ExcelCellType.TEXT, textStyle);
			createCell(workbook, sheet, row, columnCount++, d.getVoucher(), ExcelCellType.INTEGER, integerStyle);
			createCell(workbook, sheet, row, columnCount++, d.getDoctype(), ExcelCellType.TEXT, textStyle);
			createCell(workbook, sheet, row, columnCount++, d.getDra(), ExcelCellType.TEXT, textStyle);
			createCell(workbook, sheet, row, columnCount++, d.getDebitAmount(), ExcelCellType.DOUBLE, doubleStyle);
			createCell(workbook, sheet, row, columnCount++, d.getCra(), ExcelCellType.TEXT, textStyle);
			createCell(workbook, sheet, row, columnCount++, d.getCreditAmount(), ExcelCellType.DOUBLE, doubleStyle);
			createCell(workbook, sheet, row, columnCount++, d.getParticular(), ExcelCellType.RICHTEXT, textStyle);
		}

	}

	private void createCell(Workbook workbook, Sheet sheet, Row row, int columnCount, Object valueOfCell, ExcelCellType type, CellStyle style) {

		Cell cell = row.createCell(columnCount);
		if(valueOfCell == null) {
			cell.setCellValue("");
		} else {
			if (ExcelCellType.INTEGER.equals(type)) {
				if(valueOfCell instanceof Integer) {
					cell.setCellValue((Integer) valueOfCell);
				} else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.DATE.equals(type)) {
				if(valueOfCell instanceof Date) {
					cell.setCellValue((Date) valueOfCell);
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			} else if (ExcelCellType.TEXT.equals(type) || ExcelCellType.RICHTEXT.equals(type)) {
				cell.setCellValue((String) valueOfCell);
			} else if (ExcelCellType.BOOLEAN.equals(type)) {
				cell.setCellValue((Boolean) valueOfCell);
			} else if (ExcelCellType.DOUBLE.equals(type)) {
				if(valueOfCell instanceof BigDecimal) {
					cell.setCellValue(((BigDecimal) valueOfCell).doubleValue());
				}else {
					cell.setCellValue((String) valueOfCell);
				}
			}
		}

		cell.setCellStyle(style);
	}
}
