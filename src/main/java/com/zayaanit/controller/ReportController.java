package com.zayaanit.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Xscreenrpdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.enums.FormFieldType;
import com.zayaanit.enums.ReportMenu;
import com.zayaanit.model.DropdownOption;
import com.zayaanit.model.FormFieldBuilder;
import com.zayaanit.model.RadioOption;
import com.zayaanit.model.VirtualReportMenu;
import com.zayaanit.service.rp.ReportMenuBase;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 8, 2023
 */
@Slf4j
@Controller
@RequestMapping("/{rptcode}")
public class ReportController extends AbstractReportController{

	private String pageTitle = null;
	private String screenCode = null;

	@Override
	protected String screenCode() {
		return this.screenCode;
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), this.screenCode));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@SuppressWarnings("unchecked")
	@GetMapping
	public String index(@PathVariable String rptcode, HttpServletRequest request, Model model) throws Exception {
		this.screenCode = rptcode;
		model.addAttribute("isFavorite", isFavorite());

		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), rptcode));
		if(op.isPresent()) this.pageTitle = op.get().getXtitle();

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("screenCode", rptcode);
		model.addAttribute("xtitle", pageTitle);

		ReportMenuBase rm = null;
		try {
			rm = ReportMenu.valueOf(rptcode);
		} catch (Exception e) {
			log.error("=======** Report code not in system, Creating virtual Report **========");
			// Simulate virtual enum
			try {
				rm = new VirtualReportMenu(
					rptcode.toUpperCase(), 
					pageTitle, 
					rptcode.toUpperCase() + ".rpt", 
					new HashMap<>(), // or load custom paramMap from DB
					"Y", 
					false,
					"VIRTUAL"
				);
			} catch (Exception e2) {
				log.error(ERROR, e2.getMessage(), e2);
				if(!isAjaxRequest(request)) {
					return "redirect:/";
				}

				model.addAttribute("error", "Page Not found");
				model.addAttribute("status", "404");
				return "pages/404";
			}
		}

		List<FormFieldBuilder> fields = null;
		List<Xscreenrpdt> fieldsList = xscreendetailRepo.findAllByZidAndXscreen(sessionManager.getBusinessId(), rptcode);
		if(fieldsList != null && !fieldsList.isEmpty()) {
			fields = getReportFields(fieldsList);
		} else {
			if(rm.getType().equalsIgnoreCase("VIRTUAL")) {
				if(!isAjaxRequest(request)) {
					return "redirect:/";
				}

				model.addAttribute("error", "Page Not found");
				model.addAttribute("status", "404");
				return "pages/404";
			}

			fields = getReportFieldService(rm).getReportFields();
		}

		model.addAttribute("fieldsList", fields);
		model.addAttribute("reportFound", true);
		model.addAttribute("group", rm.getGroup());
		model.addAttribute("reportName", pageTitle);
		model.addAttribute("reportCode", rm.getGroup());

		return "pages/RP/RP";
	}

	private List<FormFieldBuilder> getReportFields(List<Xscreenrpdt> details) throws Exception{
		details.sort(Comparator.comparing(Xscreenrpdt::getXseqn));

		List<FormFieldBuilder> fieldsList = new ArrayList<>();

		for(Xscreenrpdt detail : details) {
			if (FormFieldType.TEXT.name().equalsIgnoreCase(detail.getXtype())) {
				String defautlValue = detail.getXdefaultvalue();
				String parsedValue = "";
				if("##zid##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getBusinessId().toString();
				if("##xstaff##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getLoggedInUserDetails().getXstaff().toString();
				fieldsList.add(FormFieldBuilder.generateTextField(detail.getXseqn(), detail.getXlabel(), parsedValue, detail.getXisrequired()));
			} else if (FormFieldType.NUMBER.name().equalsIgnoreCase(detail.getXtype())) {
				BigDecimal defautlValue = BigDecimal.valueOf(Double.valueOf(detail.getXdefaultvalue()));
				FormFieldBuilder numberField = FormFieldBuilder.generateNumberField(
					detail.getXseqn(), 
					detail.getXlabel(), 
					defautlValue, 
					detail.getXisrequired()
				);

				if(detail.getXmin() != null) {
					numberField.setMin(Integer.valueOf(detail.getXmin().toString()));
				}

				if(detail.getXmax() != null) {
					numberField.setMax(Integer.valueOf(detail.getXmax().toString()));
				}

				if(detail.getXstep() != null) {
					numberField.setStep(detail.getXstep());
				}

				fieldsList.add(numberField);
			} else if(FormFieldType.DISABLED.name().equalsIgnoreCase(detail.getXtype())) {
				String defautlValue = detail.getXdefaultvalue();
				String parsedValue = "";
				if("##zid##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getBusinessId().toString();
				if("##xstaff##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getLoggedInUserDetails().getXstaff().toString();
				fieldsList.add(FormFieldBuilder.generateDisabledField(detail.getXseqn(), detail.getXlabel(), parsedValue));
			} else if(FormFieldType.HIDDEN.name().equalsIgnoreCase(detail.getXtype())) {
				String defautlValue = detail.getXdefaultvalue();
				String parsedValue = "";
				if("##zid##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getBusinessId().toString();
				if("##xstaff##".equalsIgnoreCase(defautlValue)) parsedValue = sessionManager.getLoggedInUserDetails().getXstaff().toString();
				fieldsList.add(FormFieldBuilder.generateHiddenField(detail.getXseqn(), parsedValue));
			} else if(FormFieldType.DATE.name().equalsIgnoreCase(detail.getXtype())) {
				Date defDate = null;
				String defaultValue = detail.getXdefaultvalue().trim();
				if("TODAY".equalsIgnoreCase(defaultValue)) {
					defDate = new Date();
				} else if (StringUtils.isNotBlank(defaultValue)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					defDate = sdf.parse(defaultValue);
				}

				FormFieldBuilder dateField = FormFieldBuilder.generateDateField(
					detail.getXseqn(), 
					detail.getXisstartdate(), 
					detail.getXlabel(), 
					defDate, 
					detail.getXisrequired()
				);

				fieldsList.add(dateField);
			} else if(FormFieldType.TIME.name().equalsIgnoreCase(detail.getXtype())) {
				String defaultValue = detail.getXdefaultvalue().trim();

				FormFieldBuilder timeField = FormFieldBuilder.generateTimeField(
					detail.getXseqn(), 
					detail.getXlabel(), 
					defaultValue,
					detail.getXisrequired()
				);

				fieldsList.add(timeField);
			} else if (FormFieldType.SEARCH_ADVANCED.name().equalsIgnoreCase(detail.getXtype())) {
				fieldsList.add(
					FormFieldBuilder.generateAdvancedSearchField(
						detail.getXseqn(), 
						detail.getXlabel(), 
						"/search/table/"+ detail.getXsearchcode() +"/"+ detail.getXsearchsuffix() +"?hint=", 
						detail.getXdefaultvalue(), 
						detail.getXisrequired(),
						detail.getXdependentfieldid(),
						detail.getXresetfieldid()
					)
				);
			} else if (FormFieldType.SELECT2.name().equalsIgnoreCase(detail.getXtype()) || FormFieldType.DROPDOWN.name().equalsIgnoreCase(detail.getXtype())) {
				List<DropdownOption> options = new ArrayList<>();
				options.add(new DropdownOption("", "-- Select --"));

				if(StringUtils.isNotBlank(detail.getXoptions())) {
					String optionsString = detail.getXoptions().trim();
					String[] optionsArr = optionsString.split("\\|");
					Arrays.asList(optionsArr).stream().forEach(opt -> {
						String[] optionArr = opt.split("\\:");
						options.add(new DropdownOption(optionArr[0], optionArr[1]));
					});
				}

				if(StringUtils.isNotBlank(detail.getXoptionsquery())) {
					String sql = detail.getXoptionsquery().trim();
					sql = sql.replaceAll("##zid##", sessionManager.getBusinessId().toString());
					sql = sql.replaceAll("##xstaff##", sessionManager.getLoggedInUserDetails().getXstaff().toString());

					List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
					try {
						resultList = jdbcTemplate.queryForList(sql);
					} catch (Exception e) {
						throw new Exception(e.getCause().getMessage());
					}

					if(resultList != null && !resultList.isEmpty()) {
						for(Map<String, Object> row : resultList) {
							String value = (String) row.get("DV");
							String prompt = (String) row.get("DP");
							options.add(new DropdownOption(value, prompt));
						}
					}
				}

				if(FormFieldType.SELECT2.name().equalsIgnoreCase(detail.getXtype())) {
					fieldsList.add(FormFieldBuilder.generateSelect2(detail.getXseqn(), detail.getXlabel(), options, detail.getXdefaultvalue(), detail.getXisrequired()));
				} else {
					fieldsList.add(FormFieldBuilder.generateDropdownField(detail.getXseqn(), detail.getXlabel(), options, detail.getXdefaultvalue(), detail.getXisrequired()));
				}
			} else if (FormFieldType.RADIO.name().equalsIgnoreCase(detail.getXtype())) {
				List<RadioOption> options = new ArrayList<>();

				if(StringUtils.isNotBlank(detail.getXoptions())) {
					String optionsString = detail.getXoptions().trim();
					String[] optionsArr = optionsString.split("\\|");
					Arrays.asList(optionsArr).stream().forEach(opt -> {
						String[] optionArr = opt.split("\\:");
						options.add(new RadioOption(optionArr[0], optionArr[1]));
					});
				}

				if(StringUtils.isNotBlank(detail.getXoptionsquery())) {
					String sql = detail.getXoptionsquery().trim();
					sql = sql.replaceAll("##zid##", sessionManager.getBusinessId().toString());
					sql = sql.replaceAll("##xstaff##", sessionManager.getLoggedInUserDetails().getXstaff().toString());

					List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
					try {
						resultList = jdbcTemplate.queryForList(sql);
					} catch (Exception e) {
						throw new Exception(e.getCause().getMessage());
					}

					if(resultList != null && !resultList.isEmpty()) {
						for(Map<String, Object> row : resultList) {
							String value = (String) row.get("DV");
							String prompt = (String) row.get("DP");
							options.add(new RadioOption(value, prompt));
						}
					}
				}

				FormFieldBuilder radioField = FormFieldBuilder.generateRadioField(
					detail.getXseqn(),
					detail.getXlabel(), 
					options, 
					detail.getXdefaultvalue()
				);

				fieldsList.add(radioField);
			} else if (FormFieldType.CHECKBOX.name().equalsIgnoreCase(detail.getXtype())) {
				FormFieldBuilder checkboxField = FormFieldBuilder.generateCheckbox(
					detail.getXseqn(),
					detail.getXlabel(), 
					detail.getXisrequired()
				);

				fieldsList.add(checkboxField);
			}
		}

		return fieldsList;
	}

}
