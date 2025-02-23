package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Imadjheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImadjheaderService {

	public List<Imadjheader> LIM15(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM15(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
