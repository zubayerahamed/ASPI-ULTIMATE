package com.zayaanit.aspi.service;

import java.util.List;

import com.zayaanit.aspi.entity.Opordheader;
import com.zayaanit.aspi.enums.DatatableSortOrderType;
import com.zayaanit.aspi.model.SO13SearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpordheaderService {

	public List<Opordheader> LSO12(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LSO12(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public List<Opordheader> LSO13(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO13SearchParam param);
	public int LSO13(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO13SearchParam param);
}
