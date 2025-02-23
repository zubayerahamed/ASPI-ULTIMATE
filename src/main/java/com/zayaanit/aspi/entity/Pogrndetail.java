package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.PogrndetailPK;
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
 * Entity for GRN Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "pogrndetail")
@IdClass(PogrndetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Pogrndetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536077L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xgrnnum")
	private Integer xgrnnum;

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

	@Column(name = "xqtyord")
	private BigDecimal xqtyord;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtycrn", precision = 15, scale = 2)
	private BigDecimal xqtycrn;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Pogrndetail getDefaultInstance(Integer xgrnnum) {
		Pogrndetail obj = new Pogrndetail();
		obj.setXrow(0);
		obj.setXdocrow(0);
		obj.setXgrnnum(xgrnnum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXqtyord(BigDecimal.ZERO);
		obj.setXqtycrn(BigDecimal.ZERO);
		return obj;
	}
}
