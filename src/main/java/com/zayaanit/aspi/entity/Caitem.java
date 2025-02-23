package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.CaitemPK;
import com.zayaanit.aspi.enums.SubmitFor;

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
 * Entity for Caitem.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "caitem")
@IdClass(CaitemPK.class)
@EqualsAndHashCode(callSuper = true)
public class Caitem extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536145L;

	@Id
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xgitem", length = 25)
	private String xgitem;

	@Column(name = "xcatitem", length = 50)
	private String xcatitem;

	@Column(name = "xunit", length = 10)
	private String xunit;

	@Column(name = "xctype", length = 25)
	private String xctype;

	@Column(name = "xcost", precision = 10, scale = 2)
	private BigDecimal xcost;

	@Column(name = "xrate", precision = 10, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xispo")
	private Boolean xispo;

	@Column(name = "xisop")
	private Boolean xisop;

	@Column(name = "xbarcode", length = 50)
	private String xbarcode;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Caitem getDefaultInstance() {
		Caitem obj = new Caitem();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXctype("Weighted Average");
		obj.setXcost(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXispo(Boolean.TRUE);
		obj.setXisop(Boolean.TRUE);
		return obj;
	}
}
