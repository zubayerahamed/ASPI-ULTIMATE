package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XcodesPK;
import com.zayaanit.aspi.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed 
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xcodes")
@IdClass(XcodesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xcodes extends AbstractModel<String> {

	private static final long serialVersionUID = 405351171350622895L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xtype", length = 50)
	private String xtype;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xcode", length = 50)
	private String xcode;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@NotNull
	@Column(name = "zactive")
	private Boolean zactive;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xcodes getDefaultInstance() {
		Xcodes obj = new Xcodes();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.TRUE);
		return obj;
	}
}
