package com.zayaanit.aspi.report;

import javax.xml.bind.JAXBException;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
public interface ReportService<T> {

	public String parseXMLString(Class<T> type) throws JAXBException;
}
