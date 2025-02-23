package com.zayaanit.aspi.entity;

import java.util.ArrayList;
import java.util.List;

import com.zayaanit.aspi.entity.pk.AcmstPK;
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
@Table(name = "acmst")
@IdClass(AcmstPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acmst extends AbstractModel<String> {

	private static final long serialVersionUID = -2283958940588239713L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xacc")
	private Integer xacc;

	@Column(name = "xgroup")
	private Integer xgroup;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xacctype", length = 25)
	private String xacctype;

	@Column(name = "xaccusage", length = 25)
	private String xaccusage;

	@Transient
	private String groupName;

	@Transient
	private List<Acgroup> parentGroups = new ArrayList<>();

	@Transient
	private List<Acsub> subAccounts = new ArrayList<>();

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acmst getDefaultInstance() {
		Acmst obj = new Acmst();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
