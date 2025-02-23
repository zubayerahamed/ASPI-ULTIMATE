package com.zayaanit.aspi.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zayaanit.aspi.enums.FormFieldType;

import lombok.Data;

/**
 * @author Zubayer Ahamed
 * @since Dec 27, 2020
 */
@Data
public class FormFieldBuilder {

	private FormFieldType fieldType;
	private String prompt;
	private String fieldId;
	private String fieldName;
	private String cssClass;
	private boolean disabled;
	private boolean required;
	private int seqn;
	private int min = 0;
	private int max = 99999999;
	private int step = 1;
	private boolean checked;

	private String defaultInputValue;
	private BigDecimal defaultNumberValue;
	private Date defaultDateValue;
	private boolean startDate;
	private String defaultTime;
	private List<DropdownOption> options = new ArrayList<>();
	private List<RadioOption> radioOptions = new ArrayList<>();
	private String selectedOption;
	private String searchUrl;
	private String defaultSearchVal;
	private String searchVal;
	private String searchDes;
	private String dependentFieldId;
	private String resetFieldId;

	public void setSeqn(int seqn) {
		this.seqn = seqn;
		this.fieldId = "param" + seqn;
		this.fieldName = "param" + seqn;
	}

	/**
	 * Generate Hidden input field
	 * @param sequence
	 * @param fieldType
	 * @param defaultValue
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateHiddenField(int sequence, String defaultValue) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setFieldType(FormFieldType.HIDDEN);
		ffb.setDefaultInputValue(defaultValue);
		return ffb;
	}

	public static FormFieldBuilder generateDisabledField(int sequence, String prompt, String defaultValue) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DISABLED);
		ffb.setDefaultInputValue(defaultValue);
		return ffb;
	}

	/**
	 * Generate Input field
	 * @param sequence
	 * @param prompt
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateTextField(int sequence, String prompt, String defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.TEXT);
		ffb.setRequired(required);
		ffb.setDefaultInputValue(defaultValue);
		return ffb;
	}

	public static FormFieldBuilder generateNumberField(int sequence, String prompt, BigDecimal defaultValue, int min, int max, int step, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.NUMBER);
		ffb.setRequired(required);
		ffb.setDefaultNumberValue(defaultValue);
		ffb.setMin(min);
		ffb.setMax(max);
		ffb.setStep(step);
		return ffb;
	}

	public static FormFieldBuilder generateNumberField(int sequence, String prompt, BigDecimal defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.NUMBER);
		ffb.setRequired(required);
		ffb.setDefaultNumberValue(defaultValue);
		return ffb;
	}

	/**
	 * Generate Date field
	 * @param sequence
	 * @param prompt
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDateField(int sequence, String prompt, Date defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DATE);
		ffb.setRequired(required);
		ffb.setDefaultDateValue(defaultValue);
		return ffb;
	}

	/**
	 * Generate Date field
	 * @param sequence
	 * @param validateDateRange
	 * @param isStartDate
	 * @param prompt
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDateField(int sequence, boolean isStartDate, String prompt, Date defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DATE);
		ffb.setRequired(required);
		ffb.setDefaultDateValue(defaultValue);
		ffb.setStartDate(isStartDate);
		return ffb;
	}

	/**
	 * Generate Dropdown Field
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDropdownField(int sequence, String prompt, List<DropdownOption> options, String selectedOption, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DROPDOWN);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		return ffb;
	}

	public static FormFieldBuilder generateRadioField(int sequence, String prompt, List<RadioOption> options, String selectedOption) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.RADIO);
		ffb.setRadioOptions(options);
		ffb.setSelectedOption(selectedOption);
		return ffb;
	}

	/**
	 * Generate Select2 dropdown filed
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @return
	 */
	public static FormFieldBuilder generateSelect2(int sequence, String prompt, List<DropdownOption> options, String selectedOption, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SELECT2);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * Generate Dropdown Field
	 * @param sequence
	 * @param prompt
	 * @param options
	 * @param selectedOption
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateDropdownField(int sequence, String fieldId, String fieldName, String prompt, List<DropdownOption> options, String selectedOption, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setFieldId(fieldId);
		ffb.setFieldName(fieldName);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.DROPDOWN);
		ffb.setOptions(options);
		ffb.setSelectedOption(selectedOption);
		ffb.setRequired(required);
		return ffb;
	}

	/**
	 * Generate search field
	 * @param sequence
	 * @param prompt
	 * @param searchUrl
	 * @param defaultValue
	 * @param required
	 * @return {@link FormFieldBuilder}
	 */
	public static FormFieldBuilder generateSearchField(int sequence, String prompt, String searchUrl, String defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SEARCH);
		ffb.setSearchUrl(searchUrl);
		ffb.setDefaultSearchVal(defaultValue);
		ffb.setRequired(required);
		return ffb;
	}

	public static FormFieldBuilder generateAdvancedSearchField(int sequence, String prompt, String searchUrl, String defaultValue, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SEARCH_ADVANCED);
		ffb.setSearchUrl(searchUrl);
		ffb.setDefaultSearchVal(defaultValue);
		ffb.setRequired(required);
		ffb.setDependentFieldId(null);
		ffb.setResetFieldId(null);
		return ffb;
	}

	public static FormFieldBuilder generateAdvancedSearchField(int sequence, String prompt, String searchUrl, String defaultValue, boolean required, String dependentFieldId, String resetFieldId) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.SEARCH_ADVANCED);
		ffb.setSearchUrl(searchUrl);
		ffb.setDefaultSearchVal(defaultValue);
		ffb.setRequired(required);
		ffb.setDependentFieldId(dependentFieldId);
		ffb.setResetFieldId(resetFieldId);
		return ffb;
	}

	/**
	 * Generate Time field
	 * @param sequence
	 * @param prompt
	 * @param defaultTime
	 * @param required
	 * @return
	 */
	public static FormFieldBuilder generateTimeField(int sequence, String prompt, String defaultTime, boolean required) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.TIME);
		ffb.setRequired(required);
		ffb.setDefaultTime(defaultTime);
		return ffb;
	}

	public static FormFieldBuilder generateCheckbox(int sequence, String prompt, boolean checked) {
		FormFieldBuilder ffb = new FormFieldBuilder();
		ffb.setSeqn(sequence);
		ffb.setPrompt(prompt);
		ffb.setFieldType(FormFieldType.CHECKBOX);
		ffb.setChecked(checked);
		return ffb;
	}
}
