package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Opdoheader;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SO19SearchParam;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
public interface OpdoheaderService {

	public List<Opdoheader> LSO14(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);
	public int LSO14(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam);

	public List<Opdoheader> LSO19(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO19SearchParam param);
	public int LSO19(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SO19SearchParam param);
}
