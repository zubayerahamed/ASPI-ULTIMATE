package com.zayaanit.aspi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.aspi.entity.pk.ImopenheaderPK;
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
 * Entity for IM OPEN Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "imopenheader")
@IdClass(ImopenheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "IM_ConfirmOpening", procedureName = "IM_ConfirmOpening", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "opennum", type = Integer.class), 
	}),
})
public class Imopenheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536124L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xopennum")
	private Integer xopennum;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xtotamt", precision = 15, scale = 2)
	private BigDecimal xtotamt;

	@Column(name = "xref", length = 100)
	private String xref;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Transient
	private String businessUnitName;
	@Transient
	private String warehouseName;
	@Transient
	private String staffName;
	@Transient
	private String submitStaffName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Imopenheader getDefaultInstance() {
		Imopenheader obj = new Imopenheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusim("Open");
		obj.setXstatusjv("Open");
		return obj;
	}
}
