package com.zayaanit.aspi.entity;

import java.math.BigDecimal;

import com.zayaanit.aspi.entity.pk.ImtordetailPK;
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
 * Entity for IMTOR Detail.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imtordetail")
@IdClass(ImtordetailPK.class)
@EqualsAndHashCode(callSuper = true)
public class Imtordetail extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536109L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private Integer xtornum;

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

	public static Imtordetail getDefaultInstance(Integer xtornum) {
		Imtordetail obj = new Imtordetail();
		obj.setXtornum(xtornum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}

	public static Imtordetail getIM12DefaultInstance(Integer xtornum) {
		Imtordetail obj = new Imtordetail();
		obj.setXtornum(xtornum);
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXrow(0);
		obj.setXqty(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXlineamt(BigDecimal.ZERO);
		return obj;
	}
}
