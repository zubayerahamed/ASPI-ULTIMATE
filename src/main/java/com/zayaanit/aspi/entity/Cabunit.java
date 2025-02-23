package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.CabunitPK;
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
 * @author Zubayer Ahaned
 * @since Sep 22, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "cabunit")
@IdClass(CabunitPK.class)
@EqualsAndHashCode(callSuper = true)
public class Cabunit extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbuid ")
	private Integer xbuid;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Cabunit getDefaultInstance() {
		Cabunit obj = new Cabunit();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
