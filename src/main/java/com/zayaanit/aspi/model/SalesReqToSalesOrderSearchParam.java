package com.zayaanit.aspi.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jul 14, 2023
 */
@Data
public class SalesReqToSalesOrderSearchParam implements Serializable {

	private static final long serialVersionUID = -5834312753992259697L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date fromXdate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date toXdate;
	private Integer xcus;
	private Integer xwh;
	private Integer xstaff;
	private String xstatusreq;
	private String xitemtype;
}
