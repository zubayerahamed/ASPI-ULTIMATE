package com.zayaanit.aspi.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException;
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template) throws TransformerFactoryConfigurationError, TransformerException, FOPException;
	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException;
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map<String, Object> reportParams);
}
