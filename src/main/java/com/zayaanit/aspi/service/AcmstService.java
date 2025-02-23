package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Acmst;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface AcmstService {

	public List<Acmst> LFA13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LFA13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
