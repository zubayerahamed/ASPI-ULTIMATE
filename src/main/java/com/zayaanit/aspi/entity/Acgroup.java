package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.AcgroupPK;
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
@Table(name = "acgroup")
@IdClass(AcgroupPK.class)
@EqualsAndHashCode(callSuper = true)
public class Acgroup extends AbstractModel<String> {

	private static final long serialVersionUID = 5563603837100413499L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xagcode")
	private Integer xagcode;

	@Column(name = "xagname", length = 50)
	private String xagname;

	@Column(name = "xaglevel")
	private Integer xaglevel;

	@Column(name = "xagparent")
	private Integer xagparent;

	@Column(name = "xagtype", length = 50)
	private String xagtype;

	@Transient
	private Integer againParent;

	@Transient
	private String parentName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acgroup getDefaultInstance() {
		Acgroup obj = new Acgroup();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXaglevel(1);
		return obj;
	}
}
