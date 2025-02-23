package com.zayaanit.aspi.report;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Component;

import com.zayaanit.aspi.model.FormFieldBuilder;
import com.zayaanit.aspi.model.ResponseHelper;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Component
public interface ReportFieldService<T> {

	public List<FormFieldBuilder> getReportFields();
	public String parseXMLString(T ob) throws JAXBException;
//	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException;
//	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template) throws TransformerFactoryConfigurationError, TransformerException, FOPException;
//	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException;
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map<String, Object> reportParams);
}
