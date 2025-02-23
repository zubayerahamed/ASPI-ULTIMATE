package com.zayaanit.aspi.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.aspi.entity.pk.AcheaderPK;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "acheader")
@IdClass(AcheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "FA_VoucherPost", procedureName = "FA_VoucherPost", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "voucher", type = Integer.class) 
	}),
	@NamedStoredProcedureQuery(name = "FA_VoucherUnPost", procedureName = "FA_VoucherUnPost", parameters = {
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
		@StoredProcedureParameter(mode = ParameterMode.IN, name = "voucher", type = Integer.class) 
	}) 
})
public class Acheader extends AbstractModel<String> {

	private static final long serialVersionUID = 4696242623656256597L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xvtype", length = 25)
	private String xvtype;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xnote")
	private String xnote;

	@Column(name = "xyear")
	private Integer xyear;

	@Column(name = "xper")
	private Integer xper;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Transient
	private String businessUnitName;
	@Transient
	private String staffName;
	
	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Acheader getDefaultInstance() {
		Acheader obj = new Acheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		return obj;
	}
}
