package com.zayaanit.aspi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.zayaanit.aspi.entity.pk.TempvoucherPK;
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

/**
 * @author Zubayer Ahaned
 * @since Jan 9, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */

@Data
@Entity
@Table(name = "tempvoucher")
@IdClass(TempvoucherPK.class)
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "FA_ImportVoucher", procedureName = "FA_ImportVoucher", parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "zid", type = Integer.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "user", type = String.class),
			@StoredProcedureParameter(mode = ParameterMode.IN, name = "post", type = Boolean.class),
	}),
})
public class Tempvoucher implements Serializable {

	private static final long serialVersionUID = 1685761788841395819L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xrow")
	private Integer xrow;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE, pattern = "yyyy-MM-dd")
	@Column(name = "Voucher_Date")
	private Date voucherDate;

	@Column(name = "Business_Unit")
	private Integer businessUnit;

	@Column(name = "Debit_Acc")
	private Integer debitAcc;

	@Column(name = "Debit_SubAcc")
	private Integer debitSubAcc;

	@Column(name = "Credit_Acc")
	private Integer creditAcc;

	@Column(name = "Credit_SubAcc")
	private Integer creditSubAcc;

	@Column(name = "Amount", precision = 15, scale = 2)
	private BigDecimal amount;

	@Column(name = "Narration", length = 200)
	private String narration;

	@Column(name = "Status")
	private Boolean allOk;

	@Column(name = "ErrorDetails", columnDefinition = "TEXT")
	private String errorDetails;

	@Transient
	private String businessUnitName;
	@Transient
	private String debitAccountName;
	@Transient
	private String debitSubAccountName;
	@Transient
	private String creditAccountName;
	@Transient
	private String creditSubAccountName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Tempvoucher getDefaultInstance() {
		Tempvoucher obj = new Tempvoucher();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setVoucherDate(new Date());
		obj.setAmount(BigDecimal.ZERO);
		return obj;
	}
}
