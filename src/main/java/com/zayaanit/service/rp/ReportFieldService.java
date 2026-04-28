package com.zayaanit.service.rp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zayaanit.model.FormFieldBuilder;
import com.zayaanit.model.ResponseHelper;

import jakarta.xml.bind.JAXBException;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Component
public interface ReportFieldService<T> {

	public List<FormFieldBuilder> getReportFields();
	public String parseXMLString(T ob) throws JAXBException;
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map<String, Object> reportParams);
}
