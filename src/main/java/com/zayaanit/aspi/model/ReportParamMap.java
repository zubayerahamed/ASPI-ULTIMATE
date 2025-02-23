package com.zayaanit.aspi.model;

import java.util.HashMap;
import java.util.Map;

import com.zayaanit.aspi.enums.ReportParamDataType;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
public class ReportParamMap {

	public static final Map<String, String> voucher = new HashMap<>();
	public static final Map<String, String> poord = new HashMap<>();
	public static final Map<String, String> poordgrn = new HashMap<>();
	public static final Map<String, String> pogrn = new HashMap<>();
	public static final Map<String, String> pogrnbill = new HashMap<>();
	public static final Map<String, String> pocrn = new HashMap<>();
	public static final Map<String, String> opcrn = new HashMap<>();
	public static final Map<String, String> opord = new HashMap<>();
	public static final Map<String, String> opordinv = new HashMap<>();
	public static final Map<String, String> opdoinv = new HashMap<>();
	public static final Map<String, String> opdocln = new HashMap<>();
	public static final Map<String, String> imtor = new HashMap<>();
	public static final Map<String, String> imtorc = new HashMap<>();
	public static final Map<String, String> imiss = new HashMap<>();
	public static final Map<String, String> mobatch = new HashMap<>();
	public static final Map<String, String> imadj = new HashMap<>();
	public static final Map<String, String> imopen = new HashMap<>();
	public static final Map<String, String> oppos = new HashMap<>();
	public static final Map<String, String> opposd = new HashMap<>();
	

	public static final Map<String, String> R101 = new HashMap<>();
	public static final Map<String, String> R102 = new HashMap<>();
	public static final Map<String, String> R103 = new HashMap<>();
	public static final Map<String, String> R104 = new HashMap<>();
	public static final Map<String, String> R105 = new HashMap<>();
	public static final Map<String, String> R106 = new HashMap<>();
	public static final Map<String, String> R107 = new HashMap<>();
	public static final Map<String, String> R108 = new HashMap<>();
	public static final Map<String, String> R109 = new HashMap<>();
	public static final Map<String, String> R110 = new HashMap<>();
	public static final Map<String, String> R111 = new HashMap<>();
	public static final Map<String, String> R112 = new HashMap<>();
	public static final Map<String, String> R113 = new HashMap<>();
	public static final Map<String, String> R114 = new HashMap<>();
	public static final Map<String, String> R115 = new HashMap<>();
	public static final Map<String, String> R116 = new HashMap<>();
	public static final Map<String, String> R117 = new HashMap<>();
	public static final Map<String, String> R118 = new HashMap<>();

	public static final Map<String, String> R201 = new HashMap<>();
	public static final Map<String, String> R202 = new HashMap<>();
	public static final Map<String, String> R203 = new HashMap<>();
	public static final Map<String, String> R204 = new HashMap<>();
	public static final Map<String, String> R205 = new HashMap<>();
	public static final Map<String, String> R206 = new HashMap<>();
	public static final Map<String, String> R207 = new HashMap<>();
	public static final Map<String, String> R208 = new HashMap<>();
	public static final Map<String, String> R209 = new HashMap<>();
	public static final Map<String, String> R210 = new HashMap<>();
	public static final Map<String, String> R211 = new HashMap<>();
	public static final Map<String, String> R212 = new HashMap<>();
	public static final Map<String, String> R213 = new HashMap<>();
	public static final Map<String, String> R214 = new HashMap<>();
	public static final Map<String, String> R215 = new HashMap<>();
	public static final Map<String, String> R216 = new HashMap<>();
	public static final Map<String, String> R217 = new HashMap<>();
	public static final Map<String, String> R218 = new HashMap<>();

	public static final Map<String, String> R301 = new HashMap<>();
	public static final Map<String, String> R302 = new HashMap<>();
	public static final Map<String, String> R303 = new HashMap<>();
	public static final Map<String, String> R304 = new HashMap<>();
	public static final Map<String, String> R305 = new HashMap<>();
	public static final Map<String, String> R306 = new HashMap<>();
	public static final Map<String, String> R307 = new HashMap<>();
	public static final Map<String, String> R308 = new HashMap<>();
	public static final Map<String, String> R309 = new HashMap<>();
	public static final Map<String, String> R310 = new HashMap<>();
	public static final Map<String, String> R311 = new HashMap<>();
	public static final Map<String, String> R312 = new HashMap<>();

	public static final Map<String, String> R401 = new HashMap<>();
	public static final Map<String, String> R402 = new HashMap<>();
	public static final Map<String, String> R403 = new HashMap<>();
	public static final Map<String, String> R404 = new HashMap<>();
	public static final Map<String, String> R405 = new HashMap<>();
	public static final Map<String, String> R406 = new HashMap<>();
	public static final Map<String, String> R407 = new HashMap<>();
	public static final Map<String, String> R408 = new HashMap<>();
	public static final Map<String, String> R409 = new HashMap<>();
	public static final Map<String, String> R410 = new HashMap<>();
	public static final Map<String, String> R411 = new HashMap<>();
	public static final Map<String, String> R412 = new HashMap<>();
	public static final Map<String, String> R413 = new HashMap<>();

	public static final Map<String, String> R501 = new HashMap<>();
	public static final Map<String, String> R502 = new HashMap<>();
	public static final Map<String, String> R503 = new HashMap<>();
	public static final Map<String, String> R504 = new HashMap<>();
	public static final Map<String, String> R505 = new HashMap<>();
	public static final Map<String, String> R506 = new HashMap<>();
	public static final Map<String, String> R507 = new HashMap<>();
	public static final Map<String, String> R508 = new HashMap<>();
	public static final Map<String, String> R509 = new HashMap<>();
	public static final Map<String, String> R510 = new HashMap<>();
	public static final Map<String, String> R511 = new HashMap<>();
	public static final Map<String, String> R512 = new HashMap<>();
	public static final Map<String, String> R513 = new HashMap<>();
	public static final Map<String, String> R514 = new HashMap<>();
	public static final Map<String, String> R515 = new HashMap<>();
	public static final Map<String, String> R516 = new HashMap<>();
	public static final Map<String, String> R517 = new HashMap<>();

	static {

		voucher.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		voucher.put("param2", "xvoucher|" + ReportParamDataType.INTEGER.name());

		poord.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		poord.put("param2", "xpornum|" + ReportParamDataType.INTEGER.name());

		poordgrn.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		poordgrn.put("param2", "xpornum|" + ReportParamDataType.INTEGER.name());

		pogrn.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		pogrn.put("param2", "xgrnnum|" + ReportParamDataType.INTEGER.name());

		pogrnbill.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		pogrnbill.put("param2", "xcus|" + ReportParamDataType.INTEGER.name());
		pogrnbill.put("param3", "xinvnum|" + ReportParamDataType.STRING.name());

		pocrn.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		pocrn.put("param2", "xcrnnum|" + ReportParamDataType.INTEGER.name());

		opcrn.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opcrn.put("param2", "xcrnnum|" + ReportParamDataType.INTEGER.name());

		opord.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opord.put("param2", "xordernum|" + ReportParamDataType.INTEGER.name());

		opordinv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opordinv.put("param2", "xordernum|" + ReportParamDataType.INTEGER.name());

		opdoinv.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdoinv.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opdocln.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opdocln.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		imtor.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtor.put("param2", "xtornum|" + ReportParamDataType.INTEGER.name());

		imtorc.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imtorc.put("param2", "xtornum|" + ReportParamDataType.INTEGER.name());

		imiss.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imiss.put("param2", "xissuenum|" + ReportParamDataType.INTEGER.name());

		mobatch.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		mobatch.put("param2", "xbatch|" + ReportParamDataType.INTEGER.name());

		imadj.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imadj.put("param2", "xadjnum|" + ReportParamDataType.INTEGER.name());

		imopen.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		imopen.put("param2", "xopennum|" + ReportParamDataType.INTEGER.name());

		oppos.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		oppos.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		opposd.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		opposd.put("param2", "xdornum|" + ReportParamDataType.INTEGER.name());

		R101.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R101.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R101.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R102.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R102.put("param2", "xprofile|" + ReportParamDataType.STRING.name());
		R102.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R102.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R103.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R103.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R103.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R104.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R104.put("param2", "xgitem|" + ReportParamDataType.STRING.name());
		R104.put("param3", "xcatitem|" + ReportParamDataType.STRING.name());
		R104.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R104.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R105.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R105.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R105.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R105.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R105.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R105.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R105.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R105.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R105.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R105.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R105.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R105.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R105.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R106.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R106.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R106.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R106.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R106.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R106.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R106.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R106.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R106.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R106.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R107.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R107.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R107.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R107.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R107.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R107.put("param6", "xgsup|" + ReportParamDataType.STRING.name());
		R107.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R107.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R107.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R108.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R108.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R108.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R108.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R108.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R108.put("param6", "xgsup|" + ReportParamDataType.STRING.name());
		R108.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R108.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R108.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R109.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R109.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R109.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R109.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R109.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R109.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R109.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R109.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R109.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R109.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R109.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R109.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R109.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R110.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R110.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R110.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R110.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R110.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R110.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R110.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R110.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R110.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R110.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R111.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R111.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R111.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R111.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R111.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R111.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R111.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R111.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R111.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R112.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R112.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R112.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R112.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R112.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R112.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R112.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R112.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R112.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R113.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R113.put("param2", "xbuid|" + ReportParamDataType.INTEGER.name());
		R113.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		R113.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		R113.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		R113.put("param6", "xitem|" + ReportParamDataType.INTEGER.name());
		R113.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R113.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R114.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R114.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R114.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R114.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R114.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R114.put("param6", "xitem|" + ReportParamDataType.INTEGER.name());
		R114.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R114.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R115.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R115.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R115.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R115.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R115.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R115.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		R115.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		R115.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		R115.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R115.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R116.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R116.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R116.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R116.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R116.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R116.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		R116.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		R116.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		R116.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R116.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R117.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R117.put("param2", "xbuid|" + ReportParamDataType.INTEGER.name());
		R117.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		R117.put("param4", "xitem|" + ReportParamDataType.INTEGER.name());
		R117.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R117.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R118.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R118.put("param2", "xbuid|" + ReportParamDataType.INTEGER.name());
		R118.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		R118.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		R118.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		R118.put("param6", "xitem|" + ReportParamDataType.INTEGER.name());
		R118.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R118.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R201.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R201.put("param2", "xagcode|" + ReportParamDataType.INTEGER.name());
		R201.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R201.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R202.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R202.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R202.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R203.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R203.put("param2", "xtype|" + ReportParamDataType.STRING.name());
		R203.put("param3", "xacc|" + ReportParamDataType.INTEGER.name());
		R203.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R203.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R204.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R204.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R204.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R204.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R204.put("param5", "xtype|" + ReportParamDataType.STRING.name());
		R204.put("param6", "xvtype|" + ReportParamDataType.STRING.name());
		R204.put("param7", "xstatusjv|" + ReportParamDataType.STRING.name());
		R204.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R204.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R205.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R205.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R205.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R205.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R205.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R205.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		R205.put("param7", "xyear|" + ReportParamDataType.INTEGER.name());
		R205.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R205.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R206.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R206.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R206.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R206.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R206.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R206.put("param6", "xyear|" + ReportParamDataType.INTEGER.name());
		R206.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R206.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R207.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R207.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R207.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R207.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R207.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R207.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R207.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R208.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R208.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R208.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R208.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R208.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R208.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R208.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R209.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R209.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R209.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R209.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R209.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R209.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R209.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R210.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R210.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R210.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R210.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R210.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R210.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R210.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R211.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R211.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R211.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R211.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R211.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R211.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R211.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R212.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R212.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R212.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R212.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R212.put("param5", "xyear|" + ReportParamDataType.INTEGER.name());
		R212.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R212.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R213.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R213.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R213.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R213.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R213.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R213.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		R213.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R213.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R214.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R214.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R214.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R214.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R214.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R214.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R215.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R215.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R215.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R215.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R215.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R215.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R216.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R216.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R216.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R216.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R216.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R216.put("param6", "xsub|" + ReportParamDataType.INTEGER.name());
		R216.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R216.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R217.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R217.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R217.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R217.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R217.put("param5", "xacc|" + ReportParamDataType.INTEGER.name());
		R217.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R217.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R218.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R218.put("param2", "xdate|" + ReportParamDataType.DATE.name());
		R218.put("param3", "xbuid|" + ReportParamDataType.INTEGER.name());
		R218.put("param4", "xyear|" + ReportParamDataType.INTEGER.name());
		R218.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R218.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R301.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R301.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R301.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R301.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R301.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R301.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R301.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R301.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R301.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R301.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R301.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R301.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R301.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R302.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R302.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R302.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R302.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R302.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R302.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R302.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R302.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R302.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R302.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R303.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R303.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R303.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R303.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R303.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R303.put("param6", "xgsup|" + ReportParamDataType.STRING.name());
		R303.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R303.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R303.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R303.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R303.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R303.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R304.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R304.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R304.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R304.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R304.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R304.put("param6", "xgsup|" + ReportParamDataType.STRING.name());
		R304.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R304.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R304.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R304.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R304.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R304.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R305.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R305.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R305.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R305.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R305.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R305.put("param6", "xgsup|" + ReportParamDataType.STRING.name());
		R305.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R305.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R305.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R305.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R305.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R305.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R306.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R306.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R306.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R306.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R306.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R306.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R306.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R306.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R306.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R306.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R306.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R306.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R306.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R307.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R307.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R307.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R307.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R307.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R307.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R307.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R307.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R307.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R307.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R308.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R308.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R308.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R308.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R308.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R308.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R308.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R308.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R308.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R308.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R308.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R308.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R308.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R309.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R309.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R309.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R309.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R309.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R309.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R309.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R309.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R309.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R309.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R310.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R310.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R310.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R310.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R310.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R310.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R310.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R310.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R310.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R310.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R310.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R310.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R310.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R311.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R311.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R311.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R311.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R311.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R311.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R311.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R311.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R311.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R311.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R312.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R312.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R312.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R312.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R312.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R312.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R312.put("param7", "xgsup|" + ReportParamDataType.STRING.name());
		R312.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R312.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R312.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R312.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R312.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R312.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R401.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R401.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R401.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R401.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R401.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R401.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R401.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R401.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R401.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R401.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R401.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R401.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R401.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R402.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R402.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R402.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R402.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R402.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R402.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R402.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R402.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R402.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R402.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R403.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R403.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R403.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R403.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R403.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R403.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R403.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R403.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R403.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R403.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R403.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R403.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R404.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R404.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R404.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R404.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R404.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R404.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R404.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R404.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R404.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R404.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R404.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R404.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R405.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R405.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R405.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R405.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R405.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R405.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R405.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R405.put("param8", "xgitem|" + ReportParamDataType.STRING.name());
		R405.put("param9", "xcatitem|" + ReportParamDataType.STRING.name());
		R405.put("param10", "xitem|" + ReportParamDataType.INTEGER.name());
		R405.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R405.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R406.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R406.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R406.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R406.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R406.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R406.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R406.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R406.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R406.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R406.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R406.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R406.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R406.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R407.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R407.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R407.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R407.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R407.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R407.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R407.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R407.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R407.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R407.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R408.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R408.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R408.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R408.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R408.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R408.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R408.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R408.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R408.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R408.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R408.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R408.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R408.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R409.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R409.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R409.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R409.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R409.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R409.put("param6", "xstatusord|" + ReportParamDataType.STRING.name());
		R409.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R409.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R409.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R409.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R410.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R410.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R410.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R410.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R410.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R410.put("param6", "xgcus|" + ReportParamDataType.STRING.name());
		R410.put("param7", "xcus|" + ReportParamDataType.INTEGER.name());
		R410.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R410.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R411.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R411.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R411.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R411.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R411.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R411.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R411.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R411.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R411.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R411.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R411.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R411.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R411.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R412.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R412.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R412.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R412.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R412.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R412.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R412.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R412.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R412.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R412.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R413.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R413.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R413.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R413.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R413.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R413.put("param6", "xtype|" + ReportParamDataType.STRING.name());
		R413.put("param7", "xgcus|" + ReportParamDataType.STRING.name());
		R413.put("param8", "xcus|" + ReportParamDataType.INTEGER.name());
		R413.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R413.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R413.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R413.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R413.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R501.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R501.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R501.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R501.put("param4", "xfbuid|" + ReportParamDataType.INTEGER.name());
		R501.put("param5", "xfwh|" + ReportParamDataType.INTEGER.name());
		R501.put("param6", "xtbuid|" + ReportParamDataType.INTEGER.name());
		R501.put("param7", "xtwh|" + ReportParamDataType.INTEGER.name());
		R501.put("param8", "xtype|" + ReportParamDataType.STRING.name());
		R501.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R501.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R501.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R501.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R501.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R502.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R502.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R502.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R502.put("param4", "xfbuid|" + ReportParamDataType.INTEGER.name());
		R502.put("param5", "xfwh|" + ReportParamDataType.INTEGER.name());
		R502.put("param6", "xtbuid|" + ReportParamDataType.INTEGER.name());
		R502.put("param7", "xtwh|" + ReportParamDataType.INTEGER.name());
		R502.put("param8", "xtype|" + ReportParamDataType.STRING.name());
		R502.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R502.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R503.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R503.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R503.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R503.put("param4", "xfbuid|" + ReportParamDataType.INTEGER.name());
		R503.put("param5", "xfwh|" + ReportParamDataType.INTEGER.name());
		R503.put("param6", "xtbuid|" + ReportParamDataType.INTEGER.name());
		R503.put("param7", "xtwh|" + ReportParamDataType.INTEGER.name());
		R503.put("param8", "xtype|" + ReportParamDataType.STRING.name());
		R503.put("param9", "xgitem|" + ReportParamDataType.STRING.name());
		R503.put("param10", "xcatitem|" + ReportParamDataType.STRING.name());
		R503.put("param11", "xitem|" + ReportParamDataType.INTEGER.name());
		R503.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R503.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R504.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R504.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R504.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R504.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R504.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R504.put("param6", "xisstype|" + ReportParamDataType.STRING.name());
		R504.put("param7", "xgitem|" + ReportParamDataType.STRING.name());
		R504.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		R504.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		R504.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R504.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R505.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R505.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R505.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R505.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R505.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R505.put("param6", "xisstype|" + ReportParamDataType.STRING.name());
		R505.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R505.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R506.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R506.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R506.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R506.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R506.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R506.put("param6", "xisstype|" + ReportParamDataType.STRING.name());
		R506.put("param7", "xgitem|" + ReportParamDataType.STRING.name());
		R506.put("param8", "xcatitem|" + ReportParamDataType.STRING.name());
		R506.put("param9", "xitem|" + ReportParamDataType.INTEGER.name());
		R506.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R506.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R507.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R507.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R507.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R507.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R507.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R507.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R507.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R508.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R508.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R508.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R508.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R508.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R508.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R508.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R509.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R509.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R509.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R509.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R509.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R509.put("param6", "xcatitem|" + ReportParamDataType.STRING.name());
		R509.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		R509.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R509.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R510.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R510.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R510.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R510.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R510.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R510.put("param6", "xcatitem|" + ReportParamDataType.STRING.name());
		R510.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		R510.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R510.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R511.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R511.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R511.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R511.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R511.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R511.put("param6", "xcatitem|" + ReportParamDataType.STRING.name());
		R511.put("param7", "xitem|" + ReportParamDataType.INTEGER.name());
		R511.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R511.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R512.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R512.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R512.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R512.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R512.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R512.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		R512.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		R512.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		R512.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R512.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R513.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R513.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R513.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R513.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R513.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R513.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R513.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R514.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R514.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R514.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R514.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R514.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R514.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		R514.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		R514.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		R514.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R514.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R515.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R515.put("param2", "xbuid|" + ReportParamDataType.INTEGER.name());
		R515.put("param3", "xwh|" + ReportParamDataType.INTEGER.name());
		R515.put("param4", "xgitem|" + ReportParamDataType.STRING.name());
		R515.put("param5", "xcatitem|" + ReportParamDataType.STRING.name());
		R515.put("param6", "xitem|" + ReportParamDataType.INTEGER.name());
		R515.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R515.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R516.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R516.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R516.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R516.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R516.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R516.put("param6", "xitem|" + ReportParamDataType.INTEGER.name());
		R516.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R516.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());

		R517.put("param1", "zid|" + ReportParamDataType.INTEGER.name());
		R517.put("param2", "xfdate|" + ReportParamDataType.DATE.name());
		R517.put("param3", "xtdate|" + ReportParamDataType.DATE.name());
		R517.put("param4", "xbuid|" + ReportParamDataType.INTEGER.name());
		R517.put("param5", "xwh|" + ReportParamDataType.INTEGER.name());
		R517.put("param6", "xgitem|" + ReportParamDataType.STRING.name());
		R517.put("param7", "xcatitem|" + ReportParamDataType.STRING.name());
		R517.put("param8", "xitem|" + ReportParamDataType.INTEGER.name());
		R517.put("xtitle", "xtitle|" + ReportParamDataType.STRING.name());
		R517.put("reportType", "reportViewType|" + ReportParamDataType.STRING.name());
	}
}
