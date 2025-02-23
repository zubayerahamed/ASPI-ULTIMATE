package com.zayaanit.aspi.entity;

import com.zayaanit.aspi.entity.pk.CadocPK;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Zubayer Ahamed
 * @since Aug 6, 2023
 */
@Data
@Entity
@Table(name = "cadoc")
@IdClass(CadocPK.class)
@EqualsAndHashCode(callSuper = true)
public class Cadoc extends AbstractModel<String> {

	private static final long serialVersionUID = 2292369485864274354L;

	@Id
	@Basic(optional = false)
	@Column(name = "zid")
	private Integer zid;

	@Id
	@Basic(optional = false)
	@Column(name = "xdocid")
	private Integer xdocid;

	@Column(name = "xscreen", length = 10)
	private String xscreen;

	@Column(name = "xtrnnum")
	private Integer xtrnnum;

	@Column(name = "xtitle", length = 100)
	private String xtitle;

	@Column(name = "xdesc", length = 100)
	private String xdesc;

	@Column(name = "xdoctype", length = 10)
	private String xdoctype;

	@Column(name = "xname", length = 100)
	private String xname;

	@Column(name = "xnameold", length = 100)
	private String xnameold;

	@Column(name = "xext", length = 10)
	private String xext;

	@Column(name = "xmmyyyy", length = 10)
	private String xmmyyyy;

}
