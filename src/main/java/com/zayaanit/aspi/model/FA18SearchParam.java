package com.zayaanit.aspi.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Oct 6, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
public class FA18SearchParam implements Serializable {

	private static final long serialVersionUID = -6505241000742451543L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xfdate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xtdate;
	private Integer xyear;
	private Integer xper;
	private Integer xbuid;
	private String xtype;
	private String xstatusjv;

	private String businessUnitName;

	public static FA18SearchParam getDefaultInstance() {
		FA18SearchParam param = new FA18SearchParam();
		param.setXfdate(new Date());
		param.setXtdate(new Date());
		return param;
	}
}
