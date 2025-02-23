package com.zayaanit.aspi.report.impl;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zayaanit.aspi.model.ResponseHelper;
import com.zayaanit.aspi.repo.XcodesRepo;
import com.zayaanit.aspi.report.FOPReportService;
import com.zayaanit.aspi.report.ReportFieldService;
import com.zayaanit.aspi.service.KitSessionManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Dec 28, 2020
 */
@Slf4j
@Component
public abstract class AbstractReportService<T> implements ReportFieldService<T> {

	@Autowired protected KitSessionManager sessionManager;
	@Autowired protected FOPReportService fopReportService;
	@Autowired protected JdbcTemplate jdbcTemplate;
	@Autowired protected XcodesRepo xcodesRepo;

	protected static final SimpleDateFormat SDF = new SimpleDateFormat("E, dd-MMM-yyyy");
	protected static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm");
	protected static final String ERROR = "Error is {}, {}";
	protected static final String VALIDATION_STATUS = "status";
	protected static final String VALIDATION_MESSAGE = "message";

	@Override
	public String parseXMLString(T ob) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ob.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter result = new StringWriter();
		jaxbMarshaller.marshal(ob, result);
		log.info(result.toString());
		return result.toString();
	}

//	@Override
//	public Document getDomSourceForXML(String xml) throws ParserConfigurationException, SAXException, IOException {
//		InputSource is = new InputSource(new StringReader(xml));
//		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
//	}

//	@Override
//	public ByteArrayOutputStream transfromToPDFBytes(Document doc, String template) throws TransformerFactoryConfigurationError, TransformerException, FOPException {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		File file = new File(template);
//
//		Source xslSrc = new StreamSource(file);
//		Transformer transformer = TransformerFactory.newInstance().newTransformer(xslSrc);
//		if (transformer == null) {
//			throw new TransformerException("Template File not found: " + template);
//		}
//
//		//for image path setting
//		//String serverPath = request.getSession().getServletContext().getRealPath("/");
//
//		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
//		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
//		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
//		// Make sure the XSL transformation's result is piped through to FOP
//		Result res = new SAXResult(fop.getDefaultHandler());
//		// Start the transformation and rendering process
//		transformer.transform(new DOMSource(doc), res);
//		return out;
//	}

//	@Override
//	public byte[] getPDFReportByte(String templatePath, Map<String, Object> reportParams) throws JAXBException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException, ParseException{
//		return null;
//	}

	@Override
	public Map<String, Object> validateParams(ResponseHelper responseHelper, Map<String, Object> reportParams) {
		responseHelper.setSuccessStatusAndMessage("No validation.");
		responseHelper.setDisplayMessage(false);
		return responseHelper.getResponse();
	}
}
