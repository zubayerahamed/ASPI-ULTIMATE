package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XfavouritesPK;

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
@Table(name = "xfavourites")
@IdClass(XfavouritesPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xfavourites extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 25)
	private String zemail;

	@Id
	@Basic(optional = false)
	@Column(name = "xprofile", length = 25)
	private String xprofile;

	@Id
	@Basic(optional = false)
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xtype", length = 10)
	private String xtype;

	@Column(name = "xsequence")
	private Integer xsequence;

	@Column(name = "xisdefault", length = 1)
	private Boolean xisdefault;

	@Transient
	private String screenName;

	@Transient
	private String screenIcon;
}
