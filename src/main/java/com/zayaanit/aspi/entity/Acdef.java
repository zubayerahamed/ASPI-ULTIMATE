package com.zayaanit.aspi.entity;

import java.util.Date;

import com.zayaanit.aspi.entity.pk.AcdefPK;
import com.zayaanit.aspi.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acdef")
@IdClass(AcdefPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acdef extends AbstractModel<String> {

	private static final long serialVersionUID = -5040572499275380825L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Column(name = "xoffset")
	private Integer xoffset;

	@Column(name = "xaccpl")
	private Integer xaccpl;

	@Column(name = "xclyear")
	private Integer xclyear;

	@Temporal(TemporalType.DATE)
	@Column(name = "xcldate")
	private Date xcldate;

	@Column(name = "xaccmc")
	private Integer xaccmc;

	@Transient
	private String accountName;

	@Transient
	private String costAccountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acdef getDefaultInstance() {
		Acdef obj = new Acdef();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}

}
