package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Poordheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.PO13SearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface PoordheaderService {

	public List<Poordheader> LPO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LPO12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public List<Poordheader> LPO13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, PO13SearchParam param);
	public int LPO13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, PO13SearchParam param);
}
