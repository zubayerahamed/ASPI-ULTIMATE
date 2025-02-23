package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XwhsPK;
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
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xwhs")
@IdClass(XwhsPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xwhs extends AbstractModel<String> {

	private static final long serialVersionUID = 3403841971090795101L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xlocation", length = 100)
	private String xlocation;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xwhs getDefaultInstance() {
		Xwhs obj = new Xwhs();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
