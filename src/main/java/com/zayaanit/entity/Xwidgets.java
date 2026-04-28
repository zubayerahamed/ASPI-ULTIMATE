package com.zayaanit.entity;

import com.zayaanit.entity.pk.XwidgetsPK;
import com.zayaanit.enums.SubmitFor;

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

	@Column(name = "xdefault")
	private Integer xdefault;

	@Transient
	private Integer xseqn;

	@Transient
	private Integer widgetSize;

	@Transient
	private boolean loaddefault;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xwidgets getDefaultInstance() {
		Xwidgets obj = new Xwidgets();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdefault(1);
		return obj;
	}
}
