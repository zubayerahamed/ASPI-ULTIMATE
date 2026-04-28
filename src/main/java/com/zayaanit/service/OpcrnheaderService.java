package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opcrnheader;
import com.zayaanit.enums.DatatableSortOrderType;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpcrnheaderService {

	public List<Opcrnheader> LSO16(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public int LSO16(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
}
