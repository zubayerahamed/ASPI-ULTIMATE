package com.zayaanit.enums;

import java.util.Map;

import com.zayaanit.model.ReportParamMap;
import com.zayaanit.service.rp.ReportMenuBase;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu implements ReportMenuBase {

	// ON SCREEN REPORT
	voucher("voucher", "Voucher", "voucher.rpt", ReportParamMap.voucher, "Y", false, "SYSTEM"),
	acvouchers("acvouchers", "AC Vouchers", "acvouchers.rpt", ReportParamMap.acvouchers, "Y", false, "SYSTEM"),

	poord("poord", "Print Order", "poord.rpt", ReportParamMap.poord, "Y", false, "SYSTEM"),
	poordgrn("poordgrn", "Print GRNs", "poordgrn.rpt", ReportParamMap.poordgrn, "Y", false, "SYSTEM"),

	pogrn("pogrn", "Print GRN", "pogrn.rpt", ReportParamMap.pogrn, "Y", false, "SYSTEM"),
	pogrnbill("pogrnbill", "Print Bill", "pogrnbill.rpt", ReportParamMap.pogrnbill, "Y", false, "SYSTEM"),

	pocrn("pocrn", "Print Return", "pocrn.rpt", ReportParamMap.pocrn, "Y", false, "SYSTEM"),
	opcrn("opcrn", "Print Return", "opcrn.rpt", ReportParamMap.opcrn, "Y", false, "SYSTEM"),

	opord("opord", "Print Order", "opord.rpt", ReportParamMap.opord, "Y", false, "SYSTEM"),
	opordinv("opordinv", "Print Invoice", "opordinv.rpt", ReportParamMap.opordinv, "Y", false, "SYSTEM"),

	opdoinv("opdoinv", "Print Invoice", "opdoinv.rpt", ReportParamMap.opdoinv, "Y", false, "SYSTEM"),
	opdocln("opdocln", "Print Challan", "opdocln.rpt", ReportParamMap.opdocln, "Y", false, "SYSTEM"),

	imtor("imtor", "Print Transfer", "imtor.rpt", ReportParamMap.imtor, "Y", false, "SYSTEM"),
	imtorc("imtorc", "Print Challan", "imtorc.rpt", ReportParamMap.imtorc, "Y", false, "SYSTEM"),
	imiss("imiss", "Print Challan", "imiss.rpt", ReportParamMap.imiss, "Y", false, "SYSTEM"),
	mobatch("mobatch", "Print Batch", "mobatch.rpt", ReportParamMap.mobatch, "Y", false, "SYSTEM"),
	imadj("imadj", "Print Adjustment", "imadj.rpt", ReportParamMap.imadj, "Y", false, "SYSTEM"),
	imopen("imopen", "Print", "imopen.rpt", ReportParamMap.imopen, "Y", false, "SYSTEM"),
	imconv("imconv", "Print", "imconv.rpt", ReportParamMap.imconv, "Y", false, "SYSTEM"),

	oppos("oppos", "Print Invoice", "oppos.rpt", ReportParamMap.oppos, "Y", false, "SYSTEM"),
	opposd("opposd", "Print Invoice Duplicate", "opposd.rpt", ReportParamMap.opposd, "Y", false, "SYSTEM"),

	xlogssm("xlogssm", "Xlogs Report", "xlogssm.rpt", ReportParamMap.xlogssm, "Y", false, "SYSTEM"),
	xlogsdt("xlogsdt", "Xlogsdt Report", "xlogsdt.rpt", ReportParamMap.xlogsdt, "Y", false, "SYSTEM"),

	// REPORT MODULES REPORT
	R101("R101", "User Listing Report", "R101.rpt", ReportParamMap.R101, "Y", false, "SYSTEM"),
	R102("R102", "Profile Wise Access Report", "R102.rpt", ReportParamMap.R102, "Y", false, "SYSTEM"),
	R103("R103", "Store Listing Report", "R103.rpt", ReportParamMap.R103, "Y", false, "SYSTEM"),
	R104("R104", "Item Master Report", "R104.rpt", ReportParamMap.R104, "Y", false, "SYSTEM"),
	R105("R105", "Purchase Detail (MIS)", "R105.rpt", ReportParamMap.R105, "Y", false, "SYSTEM"),
	R106("R106", "Purchase Summary (MIS)", "R106.rpt", ReportParamMap.R106, "Y", false, "SYSTEM"),
	R107("R107", "Purchase Pending Item Ageing", "R107.rpt", ReportParamMap.R107, "Y", false, "SYSTEM"),
	R108("R108", "Purchase Pending Ageing Summary", "R108.rpt", ReportParamMap.R108, "Y", false, "SYSTEM"),
	R109("R109", "Sales Invoice Detail (MIS)", "R109.rpt", ReportParamMap.R109, "Y", false, "SYSTEM"),
	R110("R110", "Sales Invoice Summary (MIS)", "R110.rpt", ReportParamMap.R110, "Y", false, "SYSTEM"),
	R111("R111", "Sales Pending Item Ageing", "R111.rpt", ReportParamMap.R111, "Y", false, "SYSTEM"),
	R112("R112", "Sales Pending Ageing Summary", "R112.rpt", ReportParamMap.R112, "Y", false, "SYSTEM"),
	R113("R113", "Current Stock (MIS)", "R113.rpt", ReportParamMap.R113, "Y", false, "SYSTEM"),
	R114("R114", "Item Ledger Detail (MIS)", "R114.rpt", ReportParamMap.R114, "Y", false, "SYSTEM"),
	R115("R115", "Date Wise Stock Status (MIS)", "R115.rpt", ReportParamMap.R115, "Y", false, "SYSTEM"),
	R116("R116", "Item Movement Frequency", "R116.rpt", ReportParamMap.R116, "Y", false, "SYSTEM"),
	R117("R117", "Inventory Ageing Detail", "R117.rpt", ReportParamMap.R117, "Y", false, "SYSTEM"),
	R118("R118", "Inventory Ageing Summary", "R118.rpt", ReportParamMap.R118, "Y", false, "SYSTEM"),

	R201("R201", "Chart of Account Detail", "R201.rpt", ReportParamMap.R201, "Y", false, "SYSTEM"),
	R202("R202", "Chart of Account Summary", "R202.rpt", ReportParamMap.R202, "Y", false, "SYSTEM"),
	R203("R203", "Sub Account Report", "R203.rpt", ReportParamMap.R203, "Y", false, "SYSTEM"),
	R204("R204", "General Journal", "R204.rpt", ReportParamMap.R204, "Y", false, "SYSTEM"),
	R205("R205", "Account Ledger", "R205.rpt", ReportParamMap.R205, "Y", false, "SYSTEM"),
	R206("R206", "Cash/Bank Book", "R206.rpt", ReportParamMap.R206, "Y", false, "SYSTEM"),
	R207("R207", "Trail Balance Detail", "R207.rpt", ReportParamMap.R207, "Y", false, "SYSTEM"),
	R208("R208", "Trail Balance Summary", "R208.rpt", ReportParamMap.R208, "Y", false, "SYSTEM"),
	R209("R209", "Profit & Loss Detail", "R209.rpt", ReportParamMap.R209, "Y", false, "SYSTEM"),
	R210("R210", "Profit & Loss Statement", "R210.rpt", ReportParamMap.R210, "Y", false, "SYSTEM"),
	R211("R211", "Balance Sheet Detail", "R211.rpt", ReportParamMap.R211, "Y", false, "SYSTEM"),
	R212("R212", "Balance Sheet Summary", "R212.rpt", ReportParamMap.R212, "Y", false, "SYSTEM"),
	R213("R213", "Cross Year Account Ledger", "R213.rpt", ReportParamMap.R213, "Y", false, "SYSTEM"),
	R214("R214", "Cross Year Trial Balance", "R214.rpt", ReportParamMap.R214, "Y", false, "SYSTEM"),
	R215("R215", "Cross Year Profit & Loss", "R215.rpt", ReportParamMap.R215, "Y", false, "SYSTEM"),
	R216("R216", "Sub Account Ledger Detail", "R216.rpt", ReportParamMap.R216, "Y", false, "SYSTEM"),
	R217("R217", "Sub Account Ledger Summary", "R217.rpt", ReportParamMap.R217, "Y", false, "SYSTEM"),
	R218("R218", "Statement of Financial Position", "R218.rpt", ReportParamMap.R218, "Y", false, "SYSTEM"),

	R301("R301", "Purchase Order Detail", "R301.rpt", ReportParamMap.R301, "Y", false, "SYSTEM"),
	R302("R302", "Purchase Order Summary", "R302.rpt", ReportParamMap.R302, "Y", false, "SYSTEM"),
	R303("R301", "Pending Item Detail", "R303.rpt", ReportParamMap.R303, "Y", false, "SYSTEM"),
	R304("R304", "Pending Item Summary", "R304.rpt", ReportParamMap.R304, "Y", false, "SYSTEM"),
	R305("R305", "Party Pending Statement", "R305.rpt", ReportParamMap.R305, "Y", false, "SYSTEM"),
	R306("R306", "Purchase Detail", "R306.rpt", ReportParamMap.R306, "Y", false, "SYSTEM"),
	R307("R307", "Purchase Summary", "R307.rpt", ReportParamMap.R307, "Y", false, "SYSTEM"),
	R308("R308", "Purchase Item Summary", "R308.rpt", ReportParamMap.R308, "Y", false, "SYSTEM"),
	R309("R309", "Order VS GRN Summary", "R309.rpt", ReportParamMap.R309, "Y", false, "SYSTEM"),
	R310("R310", "Purchase Return Detail", "R310.rpt", ReportParamMap.R310, "Y", false, "SYSTEM"),
	R311("R311", "Purchase Return Summary", "R311.rpt", ReportParamMap.R311, "Y", false, "SYSTEM"),
	R312("R312", "Purchase  Return Item Summary", "R312.rpt", ReportParamMap.R312, "Y", false, "SYSTEM"),

	R401("R401", "Purchase Order Detail", "R401.rpt", ReportParamMap.R401, "Y", false, "SYSTEM"),
	R402("R402", "Purchase Order Summary", "R402.rpt", ReportParamMap.R402, "Y", false, "SYSTEM"),
	R403("R401", "Pending Item Detail", "R403.rpt", ReportParamMap.R403, "Y", false, "SYSTEM"),
	R404("R404", "Pending Item Summary", "R404.rpt", ReportParamMap.R404, "Y", false, "SYSTEM"),
	R405("R405", "Party Pending Statement", "R405.rpt", ReportParamMap.R405, "Y", false, "SYSTEM"),
	R406("R406", "Purchase Detail", "R406.rpt", ReportParamMap.R406, "Y", false, "SYSTEM"),
	R407("R407", "Purchase Summary", "R407.rpt", ReportParamMap.R407, "Y", false, "SYSTEM"),
	R408("R408", "Purchase Item Summary", "R408.rpt", ReportParamMap.R408, "Y", false, "SYSTEM"),
	R409("R409", "Order VS GRN Summary", "R409.rpt", ReportParamMap.R409, "Y", false, "SYSTEM"),
	R410("R410", "Purchase Return Detail", "R410.rpt", ReportParamMap.R410, "Y", false, "SYSTEM"),
	R411("R411", "Purchase Return Summary", "R411.rpt", ReportParamMap.R411, "Y", false, "SYSTEM"),
	R412("R412", "Purchase  Return Item Summary", "R412.rpt", ReportParamMap.R412, "Y", false, "SYSTEM"),
	R413("R413", "Purchase  Return Item Summary", "R413.rpt", ReportParamMap.R413, "Y", false, "SYSTEM"),

	R501("R501", "Purchase Order Detail", "R501.rpt", ReportParamMap.R501, "Y", false, "SYSTEM"),
	R502("R502", "Purchase Order Summary", "R502.rpt", ReportParamMap.R502, "Y", false, "SYSTEM"),
	R503("R503", "Pending Item Detail", "R503.rpt", ReportParamMap.R503, "Y", false, "SYSTEM"),
	R504("R504", "Pending Item Summary", "R504.rpt", ReportParamMap.R504, "Y", false, "SYSTEM"),
	R505("R505", "Party Pending Statement", "R505.rpt", ReportParamMap.R505, "Y", false, "SYSTEM"),
	R506("R506", "Purchase Detail", "R506.rpt", ReportParamMap.R506, "Y", false, "SYSTEM"),
	R507("R507", "Purchase Summary", "R507.rpt", ReportParamMap.R507, "Y", false, "SYSTEM"),
	R508("R508", "Purchase Item Summary", "R508.rpt", ReportParamMap.R508, "Y", false, "SYSTEM"),
	R509("R509", "Order VS GRN Summary", "R509.rpt", ReportParamMap.R509, "Y", false, "SYSTEM"),
	R510("R510", "Purchase Return Detail", "R510.rpt", ReportParamMap.R510, "Y", false, "SYSTEM"),
	R511("R511", "Purchase Return Summary", "R511.rpt", ReportParamMap.R511, "Y", false, "SYSTEM"),
	R512("R512", "Purchase  Return Item Summary", "R512.rpt", ReportParamMap.R512, "Y", false, "SYSTEM"),
	R513("R513", "Purchase  Return Item Summary", "R513.rpt", ReportParamMap.R513, "Y", false, "SYSTEM"),
	R514("R514", "Purchase  Return Item Summary", "R514.rpt", ReportParamMap.R514, "Y", false, "SYSTEM"),
	R515("R515", "Purchase  Return Item Summary", "R515.rpt", ReportParamMap.R515, "Y", false, "SYSTEM"),
	R516("R516", "Purchase  Return Item Summary", "R516.rpt", ReportParamMap.R516, "Y", false, "SYSTEM"),
	R517("R517", "Purchase  Return Item Summary", "R517.rpt", ReportParamMap.R517, "Y", false, "SYSTEM"),
	R518("R518", "Stock Low Status", "R518.rpt", ReportParamMap.R518, "Y", false, "SYSTEM");

	private String group;
	private String description;
	private String fileName;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean enabledFop;
	private String type;

	private ReportMenu(String group, String des, String fileName, Map<String, String> paramMap, String defaultAccess, boolean enabledFop, String type) {
		this.group = group;
		this.description = des;
		this.fileName = fileName;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.enabledFop = enabledFop;
		this.type = type;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	@Override
	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	@Override
	public boolean isEnabledFop() {
		return this.enabledFop;
	}

	@Override
	public String getType() {
		return this.type;
	}
}
