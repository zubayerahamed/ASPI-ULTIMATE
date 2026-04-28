package com.zayaanit.entity;

import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.SubmitFor;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Zubayer Ahamed
 * @since Jul 2, 2023
 */
@Data
@Entity
@Table(name = "xscreens")
@IdClass(XscreensPK.class)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
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
	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xtitle", length = 50)
	private String xtitle;

	@Column(name = "xnum")
	private Integer xnum;

	@Column(name = "xtype", length = 10)
	private String xtype;

	@Column(name = "xicon", length = 50)
	private String xicon;

	@Column(name = "xkeywords", length = 200)
	private String xkeywords;

	@Column(name = "xfile", length = 20)
	private String xfile;

	@Column(name = "xengine", length = 20)
	private String xengine;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public Xscreens(String xscreen, String xtitle, String xkeywords, String xtype) {
		this.xscreen = xscreen;
		this.xtitle = xtitle;
		this.xkeywords = xkeywords;
		this.xtype = xtype;
	}

	public static Xscreens getDefaultInstance() {
		Xscreens obj = new Xscreens();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXnum(0);
		return obj;
	}
}
