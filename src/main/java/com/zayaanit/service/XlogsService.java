package com.zayaanit.service;

import java.util.List;

import com.zayaanit.entity.Xlogs;
import com.zayaanit.enums.DatatableSortOrderType;
import com.zayaanit.model.SA17SearchParam;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
public interface XlogsService {

	public void SA17delete(SA17SearchParam param);

	public Xlogs login(HttpServletRequest request);

	public Xlogs logout(HttpServletRequest request);

	public Xlogs switchBusiness(HttpServletRequest request);

	public Xlogs switchProfile(HttpServletRequest request);

	public List<Xlogs> SA17(int limit, int offset, String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param);

	public int SA17(String orderBy, DatatableSortOrderType orderType, String searchText, int suffix, String dependentParam, SA17SearchParam param);
}
