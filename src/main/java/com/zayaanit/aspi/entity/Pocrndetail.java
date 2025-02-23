package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.PocrndetailPK;
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
 * Entity for CRN Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "pocrndetail")
@IdClass(PocrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Pocrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536081L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xcrnnum")
	private Integer xcrnnum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xqtygrn", precision = 15, scale = 2)
	private BigDecimal xqtygrn;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xrategrn", precision = 15, scale = 2)
	private BigDecimal xrategrn;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Pocrndetail getDefaultInstance(Integer xcrnnum) {
		Pocrndetail obj = new Pocrndetail();
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXcrnnum(xcrnnum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXqtygrn(BigDecimal.ZERO);
		obj.setXrategrn(BigDecimal.ZERO);
		return obj;
	}
}
