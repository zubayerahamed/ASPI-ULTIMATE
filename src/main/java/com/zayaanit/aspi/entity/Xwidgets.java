package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XwidgetsPK;

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
@Table(name = "xwidgets")
@IdClass(XwidgetsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xwidgets extends AbstractModel<String> {

	private static final long serialVersionUID = 4422439439094620583L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwidget", length = 10)
	private String xwidget;

	@Column(name = "xtitle", length = 50)
	private String xtitle;
}
