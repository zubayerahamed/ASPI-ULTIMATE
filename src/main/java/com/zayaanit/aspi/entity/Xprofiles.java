package com.zayaanit.aspi.entity;

import java.util.ArrayList;
import java.util.List;

import com.zayaanit.aspi.entity.pk.XprofilesPK;
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
 * @since Dec 1, 2020
 */
@Data
@Entity
@Table(name = "xprofiles")
@IdClass(XprofilesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xprofiles extends AbstractModel<String> {

	private static final long serialVersionUID = 2616243655037864169L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Column(name = "xnote", length = 100)
	private String xnote;

	@Transient
	private List<Xprofilesdt> details = new ArrayList<>();

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xprofiles getDefaultInstance() {
		Xprofiles obj = new Xprofiles();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
