package com.zayaanit.aspi.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.entity.Xlogsdt;
import com.zayaanit.aspi.repo.XlogsdtRepo;
import com.zayaanit.aspi.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2024
 */
@Slf4j
@Service
public class XlogsdtServiceImpl extends AbstractGenericService implements XlogsdtService {

	@Autowired private XlogsdtRepo xlogsdtRepo;
	@Autowired private AppConfig appConfig;

	@Override
	public Xlogsdt save(Xlogsdt xlogsdt) {
		if(!appConfig.isAuditEnable()) return xlogsdt;
		if(Boolean.FALSE.equals(sessionManager.getZbusiness().getXisaudit())) return xlogsdt;

		xlogsdt.setZid(sessionManager.getBusinessId());
		xlogsdt.setXsession(sessionManager.sessionId());
		xlogsdt.setXdatetime(new Date());
		xlogsdt = xlogsdtRepo.save(xlogsdt);
		log.debug("Logs Details Saved : " + xlogsdt.toString());
		return xlogsdt;
	}

}
