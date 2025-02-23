package com.zayaanit.aspi.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Jan 12, 2024
 */
@Data
public class DataDownloadAccount implements Serializable {

	private static final long serialVersionUID = -611868312617706690L;

	private String doctype; 
	private Integer voucher;
	private String vdate;
	private String dra;
	private String cra;
	private String particular;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
}
