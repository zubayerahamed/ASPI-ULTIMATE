package com.zayaanit.aspi.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zayaanit.aspi.enums.DatatableSortOrderType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Data
public class DatatableRequestHelper {

	private int draw;
	private int start;
	private int length;
	private String searchValue;
	private boolean searchRegex;
	private int orderColumnNo;
	private DatatableSortOrderType orderType;
	List<DatatableColumnDefination> columns;
	private String uniqueId;

	public DatatableRequestHelper(HttpServletRequest request) {
		this.draw = Integer.parseInt(request.getParameter("draw"));
		this.start = Integer.parseInt(request.getParameter("start"));
		this.length = Integer.parseInt(request.getParameter("length"));
		this.searchValue = String.valueOf(request.getParameter("search[value]"));
		this.searchRegex = Boolean.valueOf(request.getParameter("search[regex]"));
		this.orderColumnNo = Integer.parseInt(request.getParameter("order[0][column]"));
		this.orderType = "ASC".equalsIgnoreCase((String) request.getParameter("order[0][dir]")) ? DatatableSortOrderType.ASC : DatatableSortOrderType.DESC;
		this.uniqueId = String.valueOf(request.getParameter("_"));
		this.columns = new ArrayList<DatatableColumnDefination>();
		int noOfColumns = getNumberOfColumns(request);
		for(int i = 0; i < noOfColumns; i++) {
			String data = String.valueOf(request.getParameter("columns["+ i +"][data]"));
			String name = String.valueOf(request.getParameter("columns["+ i +"][name]"));
			boolean searchable = Boolean.parseBoolean(request.getParameter("columns["+ i +"][searchable]"));
			boolean orderable = Boolean.parseBoolean(request.getParameter("columns["+ i +"][orderable]"));
			String columnsSearchValue = String.valueOf(request.getParameter("columns["+ i +"][search][value]"));
			boolean columnSearchRegex = Boolean.parseBoolean(request.getParameter("columns["+ i +"][search][regex]"));
			DatatableColumnDefination dcd = new DatatableColumnDefination(data, name, searchable, orderable, columnsSearchValue, columnSearchRegex);
			columns.add(dcd);
		}
	}

	private int getNumberOfColumns(HttpServletRequest request) {
		int count = 0;
		Pattern p = Pattern.compile("columns\\[[0-9]+\\]\\[data\\]");
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			Matcher m = p.matcher(paramName);
			if (m.matches()) count++;
		}
		return count;
	}
}
