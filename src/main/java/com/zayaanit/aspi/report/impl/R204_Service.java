package com.zayaanit.aspi.report.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zayaanit.aspi.entity.Xcodes;
import com.zayaanit.aspi.model.DropdownOption;
import com.zayaanit.aspi.model.FormFieldBuilder;

/**
 * @author Zubayer Ahamed
 * @since Aug 29, 2023
 */
@SuppressWarnings("rawtypes")
@Service(value = "R204_Service")
public class R204_Service extends AbstractReportService {

	@Override
	public List<FormFieldBuilder> getReportFields() {
		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		fieldsList.add(FormFieldBuilder.generateHiddenField(1, sessionManager.getBusinessId().toString()));

		fieldsList.add(FormFieldBuilder.generateDateField(2, true, "From Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateDateField(3, true, "To Date", new Date(), true));

		fieldsList.add(FormFieldBuilder.generateAdvancedSearchField(4, "Business Unit", "/search/table/LAD17/0?hint=", "", false));

		List<DropdownOption> type = new ArrayList<>();
		type.add(new DropdownOption("", "-- Select --"));
		type.add(new DropdownOption("General", "General"));
		type.add(new DropdownOption("Integrated", "Integrated"));
		type.add(new DropdownOption("Imported", "Imported"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(5, "Type", type, "", false));

		List<DropdownOption> vtype = new ArrayList<>();
		vtype.add(new DropdownOption("", "-- Select --"));
		vtype.add(new DropdownOption("Journal Voucher", "Journal Voucher"));
		List<Xcodes> vtypes = xcodesRepo.findAllByXtypeAndZactiveAndZid("Voucher Type", Boolean.TRUE, sessionManager.getBusinessId());
		vtypes.stream().forEach(v -> {
			vtype.add(new DropdownOption(v.getXcode(), v.getXcode()));
		});
		fieldsList.add(FormFieldBuilder.generateDropdownField(6, "Voucher Type", vtype, "", false));

		List<DropdownOption> glstatus = new ArrayList<>();
		glstatus.add(new DropdownOption("", "-- Select --"));
		glstatus.add(new DropdownOption("Open", "Open"));
		glstatus.add(new DropdownOption("Balanced", "Balanced"));
		glstatus.add(new DropdownOption("Posted", "Posted"));
		glstatus.add(new DropdownOption("Suspended", "Suspended"));
		fieldsList.add(FormFieldBuilder.generateDropdownField(7, "GL Status", glstatus, "", false));

		return fieldsList;
	}

}
