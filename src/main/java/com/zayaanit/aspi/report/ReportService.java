package com.zayaanit.aspi.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.fop.apps.FOPException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * @author Zubayer Ahamed
 * @since Feb 16, 2021
 */
public interface ReportService<T> {

	public String parseXMLString(Class<T> type) throws JAXBException;
	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException;
	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template, HttpServletRequest request) throws TransformerFactoryConfigurationError, TransformerException, FOPException;
}
