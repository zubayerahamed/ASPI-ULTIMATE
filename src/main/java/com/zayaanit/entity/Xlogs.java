package com.zayaanit.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

/**
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xlogs")
public class Xlogs implements Serializable {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "xid")
	private Long xid;

	@Column(name = "zid")
	private Integer zid;

	@Column(name = "xsession", length = 50)
	private String xsession;

	@Column(name = "xcip", length = 50)
	private String xcip;

	@Column(name = "zemail", length = 25)
	private String zemail;

	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Column(name = "xstaff", length = 25)
	private Integer xstaff;

	@Column(name = "xaction", length = 25)
	private String xaction;

	@Column(name = "ztime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ztime;

	@Column(name = "xdate")
	@Temporal(TemporalType.DATE)
	private Date xdate;

	@Column(name = "xuseragent", length = 255)
	private String xuseragent;
}
