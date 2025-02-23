package com.zayaanit.aspi.entity;

import org.apache.commons.lang3.StringUtils;

import com.zayaanit.aspi.entity.pk.XusersPK;
import com.zayaanit.aspi.enums.SubmitFor;
import com.zayaanit.aspi.enums.UserRole;

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
@Table(name = "xusers")
@IdClass(XusersPK.class)
@EqualsAndHashCode(callSuper = true)
public class Xusers extends AbstractModel<String> {

	private static final long serialVersionUID = -2612771101344210891L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "zemail", length = 25)
	private String zemail;

	@Column(name = "xpassword", length = 25)
	private String xpassword;

	@Column(name = "xoldpassword", length = 25)
	private String xoldpassword;

	@Column(name = "xstaff")
	private Integer xstaff;

	@Column(name = "xsessiontype", length = 10)
	private String xsessiontype;

	@Column(name = "xsessiontime")
	private Integer xsessiontime;

	@Column(name = "zadmin", length = 1)
	private Boolean zadmin;

	@Column(name = "zactive", length = 1)
	private Boolean zactive = Boolean.TRUE;

	@Column(name = "xtheme", length = 10)
	private String xtheme;

	@Column(name = "xislock", length = 1)
	private Boolean xislock = Boolean.FALSE;

	@Column(name = "xbuid")
	private Integer xbuid;

	@Column(name = "xwh")
	private Integer xwh;

	@Transient
	private String roles;

	public String getRoles() {
		this.roles = "";
		if (Boolean.TRUE.equals(zadmin))
			roles += UserRole.ZADMIN.getCode() + ',';

		if (StringUtils.isBlank(roles))
			return roles;

		int lastComma = roles.lastIndexOf(',');
		String finalString = roles.substring(0, lastComma);
		roles = finalString;
		return roles;
	}

	@Transient
	private String businessName;
	@Transient
	private String employeeName;
	@Transient
	private String businessUnitName;
	@Transient
	private String warehouseName;

	@Transient
	private SubmitFor submitFor = SubmitFor.UPDATE;

	public static Xusers getDefaultInstance() {
		Xusers obj = new Xusers();
		obj.setSubmitFor(SubmitFor.INSERT);
		obj.setZactive(Boolean.TRUE);
		return obj;
	}
}
