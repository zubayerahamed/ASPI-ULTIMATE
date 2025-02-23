package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.enums.SubmitFor;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xscreens")
@IdClass(XscreensPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "Fn_getTrn", procedureName = "Fn_getTrn", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "screen", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.OUT, name = "trn_code", type = String.class) 
	}) 
})
public class Xscreens extends AbstractModel<String> {

	private static final long serialVersionUID = -2405933466608149531L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@NotBlank
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@NotBlank
	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xnum")
	private Integer xnum;

	@NotBlank
	@Column(name = "xtype", length = 10)
	private String xtype;

	@Column(name = "xicon", length = 50)
	private String xicon;

	@Column(name = "xkeywords", length = 200)
	private String xkeywords;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xscreens getDefaultInstance() {
		Xscreens obj = new Xscreens();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXnum(0);
		return obj;
	}
}
