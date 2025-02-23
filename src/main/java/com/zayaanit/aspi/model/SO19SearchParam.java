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
public class SO19SearchParam implements Serializable {

	private static final long serialVersionUID = -6505241000742451543L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xfdate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	private Date xtdate;
	private Integer xbuid;
	private Integer xwh;
	private String xstatusim;

	private String businessUnitName;
	private String warehouseName;

	public static SO19SearchParam getDefaultInstance() {
		SO19SearchParam param = new SO19SearchParam();
		param.setXfdate(new Date());
		param.setXtdate(new Date());
		return param;
	}
}
