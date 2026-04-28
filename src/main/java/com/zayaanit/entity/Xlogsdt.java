package com.zayaanit.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Builder
@Table(name = "xlogsdt")
@AllArgsConstructor
@NoArgsConstructor
public class Xlogsdt implements Serializable {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "xid")
	private Long xid;

	@Column(name = "zid", nullable = false)
	private Integer zid;

	@Column(name = "xsession", nullable = false, length = 50)
	private String xsession;

	@Column(name = "zemail", nullable = false, length = 25)
	private String zemail;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "ztime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date ztime;

	@Column(name = "xdate", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xfunc", length = 25)
	private String xfunc;

	@Column(name = "xsource", length = 25)
	private String xsource;

	@Column(name = "xtable", length = 25)
	private String xtable;

	@Column(name = "xdata", length = 50)
	private String xdata;

	@Lob
	@Column(name = "xstatement")
	private String xstatement;

	@Column(name = "xresult", length = 25)
	private String xresult;

	/**
	 * 
	 * @param xscreen
	 * @param xfunc
	 * @param xsource
	 * @param xtable
	 * @param xdata
	 * @param xstatement
	 * @param xresult
	 */
	public Xlogsdt(String xscreen, String xfunc, String xsource, String xtable, String xdata, String xstatement, String xresult) {
		this.xscreen = xscreen;
		this.xfunc = xfunc;
		this.xsource = xsource;
		this.xtable = xtable;
		this.xdata = xdata;
		this.xstatement = xstatement;
		this.xresult = xresult;
	}



	public Xlogsdt setSource(String source) {
		this.xsource = source;
		return this;
	}
}
