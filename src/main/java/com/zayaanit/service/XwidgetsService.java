package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xwidgets;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface XwidgetsService {

	public List<Xwidgets> LSA14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LSA14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
