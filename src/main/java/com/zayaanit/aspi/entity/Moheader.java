package com.zayaanit.aspi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.aspi.entity.pk.MoheaderPK;
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
 * Entity for MO Header.
 * 
 * @author Zubayer
 * @since Dec 31, 2024
 */
@Data
@Entity
@Table(name = "moheader")
@IdClass(MoheaderPK.class)
@EqualsAndHashCode(callSuper = true)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "IM_ConfirmBatch", procedureName = "IM_ConfirmBatch", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "batch", type = Integer.class), 
	}),
})
public class Moheader extends AbstractModel<String> {

	private static final long serialVersionUID = 1681419879800536137L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xbatch")
	private Integer xbatch;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xdate")
	private Date xdate;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Column(name = "xitem")
	private Integer xitem;

	@Column(name = "xqty", precision = 15, scale = 2)
	private BigDecimal xqty;

	@Column(name = "xrate", precision = 15, scale = 2)
	private BigDecimal xrate;

	@Column(name = "xexpval", precision = 15, scale = 2)
	private BigDecimal xexpval;

	@Column(name = "xstatus", length = 25)
	private String xstatus;

	@Column(name = "xstatusim", length = 25)
	private String xstatusim;

	@Column(name = "xstatusjv", length = 25)
	private String xstatusjv;

	@Column(name = "xvoucher")
	private Integer xvoucher;

	@Column(name = "xnote", length = 200)
	private String xnote;

	@Column(name = "xexptype", length = 25)
	private String xexptype;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "xsubmittime")
	private Date xsubmittime;

	@Column(name = "xstaffsubmit")
	private Integer xstaffsubmit;

	@Column(name = "xtotamt", precision = 15, scale = 2)
	private BigDecimal xtotamt;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xref")
	private String xref;

	@Transient
	private String xunit;
	@Transient
	private String businessUnitName;
	@Transient
	private String warehouseName;
	@Transient
	private String staffName;
	@Transient
	private String submitStaffName;
	@Transient
	private String itemName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Moheader getDefaultInstance() {
		Moheader obj = new Moheader();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setXdate(new Date());
		obj.setXqty(BigDecimal.ONE);
		obj.setXexptype("Percent");
		obj.setXexpval(BigDecimal.ZERO);
		obj.setXrate(BigDecimal.ZERO);
		obj.setXtotamt(BigDecimal.ZERO);
		obj.setXstatus("Open");
		obj.setXstatusjv("Open");
		obj.setXstatusim("Open");
		return obj;
	}
}
