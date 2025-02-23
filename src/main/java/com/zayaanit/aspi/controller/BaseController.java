package com.zayaanit.aspi.controller;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.model.ResponseHelper;
import com.zayaanit.aspi.service.KitSessionManager;
import com.zayaanit.aspi.service.XlogsService;
import com.zayaanit.aspi.service.XlogsdtService;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
public class BaseController {

	protected static final String ALL_BUSINESS = "ALL_ACCESSABLE_BUSINESS";
	protected static final String ERROR = "Error is : {}, {}"; 
	protected static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	protected static final SimpleDateFormat SDF2 = new SimpleDateFormat("E, dd-MMM-yyyy");
	protected static final SimpleDateFormat SDF3 = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss");
	protected static final String UTF_CODE = "UTF-8";

	@Autowired protected ApplicationContext appContext;
	@Autowired protected AppConfig appConfig;
	@Autowired protected Environment env;
	@Autowired protected KitSessionManager sessionManager;
	@Autowired protected ResponseHelper responseHelper;
	@Autowired protected XlogsService xlogsService;
	@Autowired protected XlogsdtService xlogsdtService;
}
