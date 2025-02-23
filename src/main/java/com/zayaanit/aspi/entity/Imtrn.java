package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.ImtrnPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entity for IM Transaction (Imtrn).
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imtrn")
@IdClass(ImtrnPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtrn extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536141L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "ximtrnnum")
	private Integer ximtrnnum;

	@Column(name = "xdate")
	private java.sql.Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xrate", precision = 15, scale = 3)
	private BigDecimal xrate;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xval", precision = 15, scale = 3)
	private BigDecimal xval;

	@Column(name = "xsign")
	private Integer xsign;

	@Column(name = "xrateavg", precision = 15, scale = 3)
	private BigDecimal xrateavg;

	@Column(name = "xcqtyuse", precision = 15, scale = 2)
	private BigDecimal xcqtyuse;

	@Column(name = "xdocnum")
	private Integer xdocnum;

	@Column(name = "xdoctype", length = 25)
	private String xdoctype;


	@Column(name = "xtypetrn", length = 25)
	private String xtypetrn;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	public static Imtrn getDefaultInstance() {
		Imtrn obj = new Imtrn();
		// Set default values if needed
		return obj;
	}
}
