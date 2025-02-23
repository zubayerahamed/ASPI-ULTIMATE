package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Imissueheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface ImissueheaderService {

	public List<Imissueheader> LIM13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LIM13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
