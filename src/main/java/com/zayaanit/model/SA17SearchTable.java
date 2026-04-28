package com.zayaanit.model;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Oct 21, 2025
 */
@Data
@Builder
public class SA17SearchTable {

	// xlogs fields
	private Long xid;
	private Integer zid;
	private String xsession;
	private String xcip;
	private String zemail;
	private String xprofile;
	private Integer xstaff;
	private String xaction;
	private Date ztime;
	private Date xdate;
	private String xuseragent;

	// xlogsdt fields
	private String xscreen;
	private String xfunc;
	private String xsource;
	private String xtable;
	private String xdata;
	private String xstatement;
	private String xresult;
}
