package com.zayaanit.aspi.entity;

import java.util.Base64;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
@Table(name = "zbusiness")
@EqualsAndHashCode(callSuper = true)
public class Zbusiness extends AbstractModel<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@NotBlank
	@Column(name = "zorg", length = 100)
	private String zorg;

	@Column(name = "xphone", length = 100)
	private String xphone;

	@Column(name = "xemail", length = 100)
	private String xemail;

	@Column(name = "xmadd", length = 200)
	private String xmadd;

	@Column(name = "xtin", length = 100)
	private String xtin;

	@Column(name = "xvatregno", length = 100)
	private String xvatregno;

	@Column(name = "xfilesize")
	private Integer xfilesize;

	@Column(name = "xdocpath", length = 100)
	private String xdocpath;

	@Column(name = "xdoctypes", length = 200)
	private String xdoctypes;

	@Column(name = "zactive")
	private Boolean zactive;

	@Column(name = "xisaudit")
	private Boolean xisaudit;

	@Column(name = "xsessiontime")
	private Integer xsessiontime;

	@Column(name = "xposcus")
	private Integer xposcus;

	@Column(name = "xisspeech")
	private Boolean xisspeech;

	@Lob
	@Column(name = "xlogo")
	private byte[] xlogo;

//	@Lob
//	@Column(name = "xlogodark")
//	private byte[] xlogodark;
//
//	@Column(name = "xdisplaylogo")
//	private Boolean xdisplaylogo;

	@Column(name = "xrptdefautl", length = 100)
	private String xrptdefautl;

	@Column(name = "xrptpath", length = 100)
	private String xrptpath;

	@Transient
	private String imageBase64;

//	@Transient
//	private String imageBase64Dark;

	public String getImageBase64() {
		if(this.xlogo == null || this.xlogo.length <= 0) return "";
		return Base64.getEncoder().encodeToString(this.xlogo);
	}

//	public String getImageBase64Dark() {
//		if(this.xlogodark == null || this.xlogodark.length <= 0) return "";
//		return Base64.getEncoder().encodeToString(this.xlogodark);
//	}

	@Transient
	private String customerName;
}
