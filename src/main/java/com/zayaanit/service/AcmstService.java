package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Acmst;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface AcmstService {

	public List<Acmst> LFA13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LFA13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
