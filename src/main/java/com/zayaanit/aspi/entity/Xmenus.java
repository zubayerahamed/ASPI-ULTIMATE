package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XmenusPK;
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
@Table(name = "xmenus")
@IdClass(XmenusPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xmenus extends AbstractModel<String> {

	private static final long serialVersionUID = -1624354019825186611L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xmenu", length = 10)
	private String xmenu;

	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xpmenu", length = 10)
	private String xpmenu;

	@Column(name = "xicon", length = 50)
	private String xicon;

	@Column(name = "xsequence")
	private Integer xsequence;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xmenus getDefaultInstance() {
		Xmenus obj = new Xmenus();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXsequence(0);
		return obj;
	}
}
