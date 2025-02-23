package com.zayaanit.aspi.enums;

import java.util.Map;

import com.zayaanit.aspi.model.ReportParamMap;

/**
 * @author Zubayer Ahamed
 * @since Dec 26, 2020
 */
public enum ReportMenu {

	// ON SCREEN REPORT
	voucher("FA15", "Voucher", "voucher.rpt", ReportParamMap.voucher, "Y", false),

	poord("PO12", "Print Order", "poord.rpt", ReportParamMap.poord, "Y", false),
	poordgrn("PO12", "Print GRNs", "poordgrn.rpt", ReportParamMap.poordgrn, "Y", false),

	pogrn("PO14", "Print GRN", "pogrn.rpt", ReportParamMap.pogrn, "Y", false),
	pogrnbill("PO14", "Print Bill", "pogrnbill.rpt", ReportParamMap.pogrnbill, "Y", false),

	pocrn("PO16", "Print Return", "pocrn.rpt", ReportParamMap.pocrn, "Y", false),
	opcrn("PO16", "Print Return", "opcrn.rpt", ReportParamMap.opcrn, "Y", false),

	opord("SO12", "Print Order", "opord.rpt", ReportParamMap.opord, "Y", false),
	opordinv("SO12", "Print Invoice", "opordinv.rpt", ReportParamMap.opordinv, "Y", false),

	opdoinv("SO14", "Print Invoice", "opdoinv.rpt", ReportParamMap.opdoinv, "Y", false),
	opdocln("SO14", "Print Challan", "opdocln.rpt", ReportParamMap.opdocln, "Y", false),

	imtor("IM11", "Print Transfer", "imtor.rpt", ReportParamMap.imtor, "Y", false),
	imtorc("IM11", "Print Challan", "imtorc.rpt", ReportParamMap.imtorc, "Y", false),
	imiss("IM13", "Print Challan", "imiss.rpt", ReportParamMap.imiss, "Y", false),
	mobatch("IM14", "Print Batch", "mobatch.rpt", ReportParamMap.mobatch, "Y", false),
	imadj("IM15", "Print Adjustment", "imadj.rpt", ReportParamMap.imadj, "Y", false),
	imopen("IM16", "Print", "imopen.rpt", ReportParamMap.imopen, "Y", false),

	oppos("SO18", "Print Invoice", "oppos.rpt", ReportParamMap.oppos, "Y", false),
	opposd("SO18", "Print Invoice Duplicate", "opposd.rpt", ReportParamMap.opposd, "Y", false),

	// REPORT MODULES REPORT
	R101("R101", "User Listing Report", "R101.rpt", ReportParamMap.R101, "Y", false),
	R102("R102", "Profile Wise Access Report", "R102.rpt", ReportParamMap.R102, "Y", false),
	R103("R103", "Store Listing Report", "R103.rpt", ReportParamMap.R103, "Y", false),
	R104("R104", "Item Master Report", "R104.rpt", ReportParamMap.R104, "Y", false),
	R105("R105", "Purchase Detail (MIS)", "R105.rpt", ReportParamMap.R105, "Y", false),
	R106("R106", "Purchase Summary (MIS)", "R106.rpt", ReportParamMap.R106, "Y", false),
	R107("R107", "Purchase Pending Item Ageing", "R107.rpt", ReportParamMap.R107, "Y", false),
	R108("R108", "Purchase Pending Ageing Summary", "R108.rpt", ReportParamMap.R108, "Y", false),
	R109("R109", "Sales Invoice Detail (MIS)", "R109.rpt", ReportParamMap.R109, "Y", false),
	R110("R110", "Sales Invoice Summary (MIS)", "R110.rpt", ReportParamMap.R110, "Y", false),
	R111("R111", "Sales Pending Item Ageing", "R111.rpt", ReportParamMap.R111, "Y", false),
	R112("R112", "Sales Pending Ageing Summary", "R112.rpt", ReportParamMap.R112, "Y", false),
	R113("R113", "Current Stock (MIS)", "R113.rpt", ReportParamMap.R113, "Y", false),
	R114("R114", "Item Ledger Detail (MIS)", "R114.rpt", ReportParamMap.R114, "Y", false),
	R115("R115", "Date Wise Stock Status (MIS)", "R115.rpt", ReportParamMap.R115, "Y", false),
	R116("R116", "Item Movement Frequency", "R116.rpt", ReportParamMap.R116, "Y", false),
	R117("R117", "Inventory Ageing Detail", "R117.rpt", ReportParamMap.R117, "Y", false),
	R118("R118", "Inventory Ageing Summary", "R118.rpt", ReportParamMap.R118, "Y", false),

	R201("R201", "Chart of Account Detail", "R201.rpt", ReportParamMap.R201, "Y", false),
	R202("R202", "Chart of Account Summary", "R202.rpt", ReportParamMap.R202, "Y", false),
	R203("R203", "Sub Account Report", "R203.rpt", ReportParamMap.R203, "Y", false),
	R204("R204", "General Journal", "R204.rpt", ReportParamMap.R204, "Y", false),
	R205("R205", "Account Ledger", "R205.rpt", ReportParamMap.R205, "Y", false),
	R206("R206", "Cash/Bank Book", "R206.rpt", ReportParamMap.R206, "Y", false),
	R207("R207", "Trail Balance Detail", "R207.rpt", ReportParamMap.R207, "Y", false),
	R208("R208", "Trail Balance Summary", "R208.rpt", ReportParamMap.R208, "Y", false),
	R209("R209", "Profit & Loss Detail", "R209.rpt", ReportParamMap.R209, "Y", false),
	R210("R210", "Profit & Loss Statement", "R210.rpt", ReportParamMap.R210, "Y", false),
	R211("R211", "Balance Sheet Detail", "R211.rpt", ReportParamMap.R211, "Y", false),
	R212("R212", "Balance Sheet Summary", "R212.rpt", ReportParamMap.R212, "Y", false),
	R213("R213", "Cross Year Account Ledger", "R213.rpt", ReportParamMap.R213, "Y", false),
	R214("R214", "Cross Year Trial Balance", "R214.rpt", ReportParamMap.R214, "Y", false),
	R215("R215", "Cross Year Profit & Loss", "R215.rpt", ReportParamMap.R215, "Y", false),
	R216("R216", "Sub Account Ledger Detail", "R216.rpt", ReportParamMap.R216, "Y", false),
	R217("R217", "Sub Account Ledger Summary", "R217.rpt", ReportParamMap.R217, "Y", false),
	R218("R218", "Statement of Financial Position", "R218.rpt", ReportParamMap.R218, "Y", false),

	R301("R301", "Purchase Order Detail", "R301.rpt", ReportParamMap.R301, "Y", false),
	R302("R302", "Purchase Order Summary", "R302.rpt", ReportParamMap.R302, "Y", false),
	R303("R301", "Pending Item Detail", "R303.rpt", ReportParamMap.R303, "Y", false),
	R304("R304", "Pending Item Summary", "R304.rpt", ReportParamMap.R304, "Y", false),
	R305("R305", "Party Pending Statement", "R305.rpt", ReportParamMap.R305, "Y", false),
	R306("R306", "Purchase Detail", "R306.rpt", ReportParamMap.R306, "Y", false),
	R307("R307", "Purchase Summary", "R307.rpt", ReportParamMap.R307, "Y", false),
	R308("R308", "Purchase Item Summary", "R308.rpt", ReportParamMap.R308, "Y", false),
	R309("R309", "Order VS GRN Summary", "R309.rpt", ReportParamMap.R309, "Y", false),
	R310("R310", "Purchase Return Detail", "R310.rpt", ReportParamMap.R310, "Y", false),
	R311("R311", "Purchase Return Summary", "R311.rpt", ReportParamMap.R311, "Y", false),
	R312("R312", "Purchase  Return Item Summary", "R312.rpt", ReportParamMap.R312, "Y", false),

	R401("R401", "Purchase Order Detail", "R401.rpt", ReportParamMap.R401, "Y", false),
	R402("R402", "Purchase Order Summary", "R402.rpt", ReportParamMap.R402, "Y", false),
	R403("R401", "Pending Item Detail", "R403.rpt", ReportParamMap.R403, "Y", false),
	R404("R404", "Pending Item Summary", "R404.rpt", ReportParamMap.R404, "Y", false),
	R405("R405", "Party Pending Statement", "R405.rpt", ReportParamMap.R405, "Y", false),
	R406("R406", "Purchase Detail", "R406.rpt", ReportParamMap.R406, "Y", false),
	R407("R407", "Purchase Summary", "R407.rpt", ReportParamMap.R407, "Y", false),
	R408("R408", "Purchase Item Summary", "R408.rpt", ReportParamMap.R408, "Y", false),
	R409("R409", "Order VS GRN Summary", "R409.rpt", ReportParamMap.R409, "Y", false),
	R410("R410", "Purchase Return Detail", "R410.rpt", ReportParamMap.R410, "Y", false),
	R411("R411", "Purchase Return Summary", "R411.rpt", ReportParamMap.R411, "Y", false),
	R412("R412", "Purchase  Return Item Summary", "R412.rpt", ReportParamMap.R412, "Y", false),
	R413("R413", "Purchase  Return Item Summary", "R413.rpt", ReportParamMap.R413, "Y", false),

	R501("R501", "Purchase Order Detail", "R501.rpt", ReportParamMap.R501, "Y", false),
	R502("R502", "Purchase Order Summary", "R502.rpt", ReportParamMap.R502, "Y", false),
	R503("R501", "Pending Item Detail", "R503.rpt", ReportParamMap.R503, "Y", false),
	R504("R504", "Pending Item Summary", "R504.rpt", ReportParamMap.R504, "Y", false),
	R505("R505", "Party Pending Statement", "R505.rpt", ReportParamMap.R505, "Y", false),
	R506("R506", "Purchase Detail", "R506.rpt", ReportParamMap.R506, "Y", false),
	R507("R507", "Purchase Summary", "R507.rpt", ReportParamMap.R507, "Y", false),
	R508("R508", "Purchase Item Summary", "R508.rpt", ReportParamMap.R508, "Y", false),
	R509("R509", "Order VS GRN Summary", "R509.rpt", ReportParamMap.R509, "Y", false),
	R510("R510", "Purchase Return Detail", "R510.rpt", ReportParamMap.R510, "Y", false),
	R511("R511", "Purchase Return Summary", "R511.rpt", ReportParamMap.R511, "Y", false),
	R512("R512", "Purchase  Return Item Summary", "R512.rpt", ReportParamMap.R512, "Y", false),
	R513("R513", "Purchase  Return Item Summary", "R513.rpt", ReportParamMap.R513, "Y", false),
	R514("R514", "Purchase  Return Item Summary", "R514.rpt", ReportParamMap.R514, "Y", false),
	R515("R515", "Purchase  Return Item Summary", "R515.rpt", ReportParamMap.R515, "Y", false),
	R516("R516", "Purchase  Return Item Summary", "R516.rpt", ReportParamMap.R516, "Y", false),
	R517("R517", "Purchase  Return Item Summary", "R517.rpt", ReportParamMap.R517, "Y", false);

	private String group;
	private String description;
	private String fileName;
	private Map<String, String> paramMap;
	private String defaultAccess;
	private boolean enabledFop;

	private ReportMenu(String group, String des, String fileName, Map<String, String> paramMap, String defaultAccess, boolean enabledFop) {
		this.group = group;
		this.description = des;
		this.fileName = fileName;
		this.paramMap = paramMap;
		this.defaultAccess = defaultAccess;
		this.enabledFop = enabledFop;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getGroup() {
		return this.group;
	}

	public String getDescription() {
		return this.description;
	}

	public Map<String, String> getParamMap() {
		return this.paramMap;
	}

	public String getDefaultAccess() {
		return this.defaultAccess;
	}

	public boolean isEnabledFop() {
		return this.enabledFop;
	}
}
