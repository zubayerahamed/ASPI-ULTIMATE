package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XprofilesdtPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since 02-12-2020
 */
@Data
@Entity
@Table(name = "xprofilesdt")
@IdClass(XprofilesdtPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xprofilesdt extends AbstractModel<String> {

	private static final long serialVersionUID = -9011140154721641297L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Id
	@Basic(optional = false)
	@Column(name = "xmenu", length = 10)
	private String xmenu;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;
}
