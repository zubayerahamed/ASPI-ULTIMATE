package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.AcsubPK;
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
 * @author Zubayer Ahamed
 * @since Aug 7, 2024
 */
@Data
@Entity
@Table(name = "acsub")
@IdClass(AcsubPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acsub extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536070L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xsub")
	private Integer xsub;

	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xgcus", length = 25)
	private String xgcus;

	@Column(name = "xgsup", length = 25)
	private String xgsup;

	@Transient
	private String accountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acsub getDefaultInstance() {
		Acsub obj = new Acsub();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
