package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XuserwidgetsPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahaned
 * @since Dec 30, 2024
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Data
@Entity
@Table(name = "xuserwidgets")
@IdClass(XuserwidgetsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xuserwidgets extends AbstractModel<String> {

	private static final long serialVersionUID = -5662780920398558237L;

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
	@Column(name = "xwidget", length = 10)
	private String xwidget;

	@Column(name = "xsequence")
	private Integer xsequence;
}
