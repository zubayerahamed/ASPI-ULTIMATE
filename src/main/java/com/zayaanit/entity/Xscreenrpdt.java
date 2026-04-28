package com.zayaanit.entity;

import java.math.BigDecimal;

import com.zayaanit.entity.pk.XscreenrpdtPK;
import com.zayaanit.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since May 8, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xscreenrpdt")
@IdClass(XscreenrpdtPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xscreenrpdt extends AbstractModel<String> {

	private static final long serialVersionUID = -7051210656876929495L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow", length = 10)
	private Integer xrow;

	@Column(name = "xtype", length = 20)
	private String xtype;

	@Column(name = "xseqn")
	private Integer xseqn;

	@Column(name = "xlabel", length = 50)
	private String xlabel;

	@Column(name = "xisdisable")
	private Boolean xisdisable;

	@Column(name = "xisrequired")
	private Boolean xisrequired;

	@Column(name = "xisstartdate")
	private Boolean xisstartdate;

	@Column(name = "xisenddate")
	private Boolean xisenddate;

	@Column(name = "xmin")
	private BigDecimal xmin;

	@Column(name = "xmax")
	private BigDecimal xmax;

	@Column(name = "xstep")
	private Integer xstep;

	@Column(name = "xdefaultvalue", length = 50)
	private String xdefaultvalue;

	@Column(name = "xoptionsquery", length = 200)
	private String xoptionsquery;

	@Column(name = "xoptions", length = 200)
	private String xoptions;

	@Column(name = "xsearchcode", length = 10)
	private String xsearchcode;

	@Column(name = "xsearchsuffix", length = 10)
	private String xsearchsuffix;

	@Column(name = "xdependentfieldid", length = 10)
	private String xdependentfieldid;

	@Column(name = "xresetfieldid", length = 10)
	private String xresetfieldid;

	@Column(name = "xparamtype", length = 20)
	private String xparamtype;

	@Column(name = "xrparam", length = 20)
	private String xrparam;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xscreenrpdt getDefaultInstance(String xscreen) {
		Xscreenrpdt obj = new Xscreenrpdt();
		obj.setXrow(0);
		obj.setXscreen(xscreen);
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
