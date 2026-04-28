package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Imadjheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImadjheaderService {

	public List<Imadjheader> LIM15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM15(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
