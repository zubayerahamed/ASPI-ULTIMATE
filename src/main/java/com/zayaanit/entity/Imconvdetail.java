package com.zayaanit.entity;

import java.math.BigDecimal;

import com.zayaanit.entity.pk.ImconvdetailPK;
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
 * Entity for MO Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imconvdetail")
@IdClass(ImconvdetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imconvdetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536139L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xconvnum")
	private Integer xconvnum;

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

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private String xunit;

	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imconvdetail getRAWDefaultInstance(Integer xconvnum) {
		Imconvdetail obj = new Imconvdetail();
		obj.setXconvnum(xconvnum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
