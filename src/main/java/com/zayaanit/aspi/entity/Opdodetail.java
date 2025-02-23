package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.OpdodetailPK;
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
 * Entity for DO Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "opdodetail")
@IdClass(OpdodetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Opdodetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536092L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdornum")
	private Integer xdornum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xdocrow")
	private Integer xdocrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqtyord", precision = 15, scale = 2)
	private BigDecimal xqtyord;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtycrn", precision = 15, scale = 2)
	private BigDecimal xqtycrn;

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

	public static Opdodetail getDefaultInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXdornum(xdornum);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXqtyord(BigDecimal.ZERO);
		obj.setXqtycrn(BigDecimal.ZERO);
		obj.setXrategrn(BigDecimal.ZERO);
		return obj;
	}

	public static Opdodetail getPOSInstance(Integer xdornum) {
		Opdodetail obj = new Opdodetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXdornum(xdornum);
		obj.setXqty(BigDecimal.ONE);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		obj.setXqtyord(BigDecimal.ZERO);
		obj.setXqtycrn(BigDecimal.ZERO);
		obj.setXrategrn(BigDecimal.ZERO);
		return obj;
	}
}
