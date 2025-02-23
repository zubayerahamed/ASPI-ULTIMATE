package com.zayaanit.aspi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.aspi.entity.pk.ImtorheaderPK;
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

/**
 * Entity for IMTOR Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imtorheader")
@IdClass(ImtorheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "IM_ConfirmDirectTO", procedureName = "IM_ConfirmDirectTO", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "tornum", type = Integer.class), 
	}),
	@NamedStoredProcedureQuery(name = "IM_ConfirmBusinessTO", procedureName = "IM_ConfirmBusinessTO", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "tornum", type = Integer.class), 
	}),
})
public class Imtorheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536107L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xtornum")
	private Integer xtornum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xfbuid")
	private Integer xfbuid;

	@Column(name = "xtbuid")
	private Integer xtbuid;

	@Column(name = "xfwh")
	private Integer xfwh;

	@Column(name = "xtwh")
	private Integer xtwh;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xtotamt", precision = 15, scale = 2)
	private BigDecimal xtotamt;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xfvoucher")
	private Integer xfvoucher;

	@Column(name = "xtvoucher")
	private Integer xtvoucher;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xtype", length = 25)
	private String xtype;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Transient
	private String fromBusinessUnitName;
	@Transient
	private String toBusinessUnitName;
	@Transient
	private String fromWarehouseName;
	@Transient
	private String toWarehouseName;
	@Transient
	private String staffName;
	@Transient
	private String submitStaffName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imtorheader getDefaultInstance() {
		Imtorheader obj = new Imtorheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXtype("Direct Transfer");
		return obj;
	}

	public static Imtorheader getIM12DefaultInstance() {
		Imtorheader obj = new Imtorheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXstatusjv("Open");
		obj.setXtype("Inter Business");
		return obj;
	}
}
