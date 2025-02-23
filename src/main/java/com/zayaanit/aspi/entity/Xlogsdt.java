package com.zayaanit.aspi.entity;

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
@Table(name = "xlogsdt")
@AllArgsConstructor
@NoArgsConstructor
public class Xlogsdt implements Serializable {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "zid", nullable = false)
	private Integer zid;

	@Column(name = "xsession", nullable = false, length = 150)
	private String xsession;

	@Column(name = "xdatetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date xdatetime;

	@Column(name = "xscreen", length = 25)
	private String xscreen;

	@Column(name = "xfunc", length = 25)
	private String xfunc;

	@Column(name = "xsource", length = 25)
	private String xsource;

	@Column(name = "xfuncdt", length = 25)
	private String xfuncdt;

	@Column(name = "xdata")
	private String xdata;

	@Lob
	@Column(name = "xstatement")
	private String xstatement;

	@Column(name = "xmessage", length = 50)
	private String xmessage;

	@Column(name = "xresult", length = 25)
	private String xresult;

	public Xlogsdt(String xscreen, String xfunc, String xsource, String xfuncdt, String xdata, String xstatement, String xmessage, String xresult) {
		this.xscreen = xscreen;
		this.xfunc = xfunc;
		this.xsource = xsource;
		this.xfuncdt = xfuncdt;
		this.xdata = xdata;
		this.xstatement = xstatement;
		this.xmessage = xmessage;
		this.xresult = xresult;
	}

	/**
	 * @param screenCode
	 * @param operation
	 * @param title
	 * @param reference
	 * @param data
	 * @param detail
	 * @param index
	 */
	public Xlogsdt(String screenCode, String operation, String title, String reference, String data, boolean detail, int index) {
		this.xscreen = screenCode;
		this.xfunc = operation;
		this.xsource = screenCode;
		this.xfuncdt = operation + " " + title;
		if(detail) this.xfuncdt += " detail " + index;
		this.xdata = reference;
		this.xstatement = data;
		this.xmessage = null;
		this.xresult = "Success";
	}

	public Xlogsdt setSource(String source) {
		this.xsource = source;
		return this;
	}

	public Xlogsdt setMessage(String message) {
		this.xmessage = message;
		return this;
	}
}
