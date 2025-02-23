package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Acsub;
import com.zayaanit.aspi.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface AcsubService {

	public List<Acsub> LFA14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LFA14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
