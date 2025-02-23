package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.OporddetailPK;
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
 * Entity for Order Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "oporddetail")
@IdClass(OporddetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Oporddetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536085L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xordernum")
	private Integer xordernum;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xlineamt", precision = 15, scale = 2)
	private BigDecimal xlineamt;

	@Column(name = "xqtydel", precision = 15, scale = 2)
	private BigDecimal xqtydel;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Oporddetail getDefaultInstance(Integer xordernum) {
		Oporddetail obj = new Oporddetail();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXordernum(xordernum);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXqtydel(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
