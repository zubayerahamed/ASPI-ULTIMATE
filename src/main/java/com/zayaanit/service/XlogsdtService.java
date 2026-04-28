package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SA17SearchParam;
import com.zayaanit.model.XlogsdtEvent;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public interface XlogsdtService {

	public void SA17delete(SA17SearchParam param);

	public void save(XlogsdtEvent event);

	public List<Xlogsdt> SA17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param);
	public int SA17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param);
}
