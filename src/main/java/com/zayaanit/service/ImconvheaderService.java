package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imconvheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahaned
 * @since Nov 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
public interface ImconvheaderService {

	public List<Imconvheader> LIM17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
