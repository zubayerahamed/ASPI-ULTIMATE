package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.OpcrndetailPK;
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
@Table(name = "opcrndetail")
@IdClass(OpcrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opcrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536105L;

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

	@Column(name = "xqtyinv", precision = 15, scale = 2)
	private BigDecimal xqtyinv;

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

	public static Opcrndetail getDefaultInstance(Integer xcrnnum) {
		Opcrndetail obj = new Opcrndetail();
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXcrnnum(xcrnnum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXqtyinv(BigDecimal.ZERO);
		obj.setXrategrn(BigDecimal.ZERO);
		return obj;
	}
}
