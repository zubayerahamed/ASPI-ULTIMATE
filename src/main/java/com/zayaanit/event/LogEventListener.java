package com.zayaanit.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.service.XlogsdtService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Oct 19, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
@Slf4j
public class LogEventListener {

	@Autowired private XlogsdtService xlogsdtService;

	@Async 
	@EventListener
	@Order(1)
	public void handleLogDetailsEvent(XlogsdtEvent event) {
		xlogsdtService.save(event);
	}
}
