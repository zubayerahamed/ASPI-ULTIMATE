package com.zayaanit.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.entity.Caitem;
import com.zayaanit.entity.Opdodetail;
import com.zayaanit.entity.Opdoheader;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.CaitemPK;
import com.zayaanit.entity.pk.OpdoheaderPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.DatatableRequestHelper;
import com.zayaanit.model.DatatableResponseHelper;
import com.zayaanit.model.ReloadSection;
import com.zayaanit.model.ReloadSectionParams;
import com.zayaanit.model.SO19SearchParam;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.CaitemRepo;
import com.zayaanit.repo.OpdodetailRepo;
import com.zayaanit.repo.OpdoheaderRepo;
import com.zayaanit.service.OpdoheaderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahaned
 * @since Jan 23, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Slf4j
@Controller
@RequestMapping("/SO19")
public class SO19 extends KitController {

	@Autowired private OpdoheaderRepo opdoheaderRepo;
	@Autowired private OpdodetailRepo opdodetailRepo;
	@Autowired private OpdoheaderService opdoheaderService;
	@Autowired private CaitemRepo caitemRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SO19";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SO19"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, @RequestParam(required = false) String frommenu, Model model) {
		model.addAttribute("searchParam", SO19SearchParam.getDefaultInstance());

		if(isAjaxRequest(request) && frommenu == null) {
			return "pages/SO19/SO19-fragments::main-form";
		}

		if(frommenu == null) return "redirect:/";

		return "pages/SO19/SO19";
	}

	@PostMapping("/header-table")
	public String loadHeaderTableFragment(SO19SearchParam param, Model model){
		model.addAttribute("searchParam", param);
		return "pages/SO19/SO19-fragments::header-table";
	}

	@GetMapping("/all")
	public @ResponseBody DatatableResponseHelper<Opdoheader> getAll(
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) String xstatusim
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO19SearchParam param = new SO19SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXstatusim(xstatusim);

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("SO19")
					.xfunc("Search")
					.xsource("SO19")
					.xtable(null)
					.xdata(null)
					.xstatement("Search : " + param.toString())
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		DatatableRequestHelper helper = new DatatableRequestHelper(request);

		List<Opdoheader> list = opdoheaderService.LSO19(helper.getLength(), helper.getStart(), helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);
		int	totalRows = opdoheaderService.LSO19(helper.getColumns().get(helper.getOrderColumnNo()).getName(), helper.getOrderType(), helper.getSearchValue(), 0, null, param);

		DatatableResponseHelper<Opdoheader> response = new DatatableResponseHelper<>();
		response.setDraw(helper.getDraw());
		response.setRecordsTotal(totalRows);
		response.setRecordsFiltered(totalRows);
		response.setData(list);
		return response;
	}

	@Transactional
	@PostMapping("/invoice-confirm")
	public @ResponseBody Map<String, Object> invoiceConfirm(
		@RequestParam Integer xdornum,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) String xstatusim
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO19SearchParam param = new SO19SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXstatusim(xstatusim);

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = opdoheaderOp.get();
		if(!"Confirmed".equals(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Invoice status not confirmed");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(opdoheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Invoice inventory status not open");
			return responseHelper.getResponse();
		}

		List<Opdodetail> details = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), xdornum);
		if(details == null || details.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("Invoice don't have any item");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXdate() == null) {
			responseHelper.setErrorStatusAndMessage("Date required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXbuid() == null) {
			responseHelper.setErrorStatusAndMessage("Business unit required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXcus() == null) {
			responseHelper.setErrorStatusAndMessage("Customer required");
			return responseHelper.getResponse();
		}

		if(opdoheader.getXwh() == null) {
			responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
			return responseHelper.getResponse();
		}

		// Check qty is exist in all details 
		BigDecimal totalQty = BigDecimal.ZERO;
		for(Opdodetail detail : details) {
			if(detail.getXqty() == null) continue;
			totalQty = totalQty.add(detail.getXqty());
		}
		if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
			responseHelper.setErrorStatusAndMessage("No items found!");
			return responseHelper.getResponse();
		}

		// check inventory
		Map<Integer, BigDecimal> qtyMap = new HashMap<>();
		for(Opdodetail item : details) {
			Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), item.getXitem()));
			if(!caitemOp.isPresent()) continue;
			if("Services".equals(caitemOp.get().getXgitem())) continue;

			if(qtyMap.get(item.getXitem()) != null) {
				BigDecimal prevQty = qtyMap.get(item.getXitem());
				BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				qtyMap.put(item.getXitem(), newQty);
			} else {
				qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
			}
		}

		prepareUnavailableStockList(qtyMap, opdoheader.getXbuid(), opdoheader.getXwh());

		if(!unavailableStockList.isEmpty()) {
			responseHelper.setShowErrorDetailModal(true);
			responseHelper.setErrorDetailsList(unavailableStockList);
			responseHelper.setErrorStatusAndMessage("Stock not available");
			responseHelper.setReloadSectionIdWithUrl("error-details-container", "/SO19/error-details");
			return responseHelper.getResponse();
		}

		try {
			opdoheaderRepo.SO_ConfirmInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), opdoheader.getXdornum());
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		eventPublisher.publishEvent(
				new XlogsdtEvent(
					Xlogsdt.builder()
					.xscreen("SO19")
					.xfunc("Confirm Invoice")
					.xsource("SO19")
					.xtable(null)
					.xdata(null)
					.xstatement("Confirm Invoice : SO_ConfirmInvoice(" + sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ opdoheader.getXdornum() +")")
					.xresult("Success")
					.build(), 
					sessionManager
				)
			);

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusim", xstatusim));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO19/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoice confirmed successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/confirm-all")
	public @ResponseBody Map<String, Object> confirmAllInvoices(
		@RequestParam String selectedInvoices,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) String xstatusim
		) {

		if(StringUtils.isBlank(selectedInvoices)) {
			responseHelper.setErrorStatusAndMessage("No invoice seleted");
			return responseHelper.getResponse();
		}

		List<String> invoices = Arrays.asList(selectedInvoices.split(","));
		if(invoices.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No invoice seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO19SearchParam param = new SO19SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXstatusim(xstatusim);

		List<Opdoheader> allValidInvoices = new ArrayList<>();

		for(String invoice : invoices) {
			Optional<Opdoheader> invoiceOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(invoice)));
			if(invoiceOp.isPresent()) {
				Opdoheader opdoheader = invoiceOp.get();
				if("Confirmed".equals(opdoheader.getXstatus()) && "Open".equals(opdoheader.getXstatusim())) {
					allValidInvoices.add(opdoheader);
				}
			}
		}

		if(allValidInvoices.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any valid invoice to do confirm");
			return responseHelper.getResponse();
		}

		for(Opdoheader opdoheader : allValidInvoices) {
			List<Opdodetail> details = opdodetailRepo.findAllByZidAndXdornum(sessionManager.getBusinessId(), opdoheader.getXdornum());
			if(details == null || details.isEmpty()) {
				responseHelper.setErrorStatusAndMessage("Invoice don't have any item");
				return responseHelper.getResponse();
			}

			if(opdoheader.getXdate() == null) {
				responseHelper.setErrorStatusAndMessage("Date required");
				return responseHelper.getResponse();
			}

			if(opdoheader.getXbuid() == null) {
				responseHelper.setErrorStatusAndMessage("Business unit required");
				return responseHelper.getResponse();
			}

			if(opdoheader.getXcus() == null) {
				responseHelper.setErrorStatusAndMessage("Customer required");
				return responseHelper.getResponse();
			}

			if(opdoheader.getXwh() == null) {
				responseHelper.setErrorStatusAndMessage("Store/Warehouse required");
				return responseHelper.getResponse();
			}

			// Check qty is exist in all details 
			BigDecimal totalQty = BigDecimal.ZERO;
			for(Opdodetail detail : details) {
				if(detail.getXqty() == null) continue;
				totalQty = totalQty.add(detail.getXqty());
			}
			if(totalQty.compareTo(BigDecimal.ZERO) == 0) {
				responseHelper.setErrorStatusAndMessage("No items found!");
				return responseHelper.getResponse();
			}

			// check inventory
			Map<Integer, BigDecimal> qtyMap = new HashMap<>();
			for(Opdodetail item : details) {
				Optional<Caitem> caitemOp = caitemRepo.findById(new CaitemPK(sessionManager.getBusinessId(), item.getXitem()));
				if(!caitemOp.isPresent()) continue;
				if("Services".equals(caitemOp.get().getXgitem())) continue;

				if(qtyMap.get(item.getXitem()) != null) {
					BigDecimal prevQty = qtyMap.get(item.getXitem());
					BigDecimal newQty = prevQty.add(item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
					qtyMap.put(item.getXitem(), newQty);
				} else {
					qtyMap.put(item.getXitem(), item.getXqty() == null ? BigDecimal.ZERO : item.getXqty());
				}
			}

			prepareUnavailableStockList(qtyMap, opdoheader.getXbuid(), opdoheader.getXwh());

			if(!unavailableStockList.isEmpty()) {
				responseHelper.setShowErrorDetailModal(true);
				responseHelper.setErrorDetailsList(unavailableStockList);
				responseHelper.setErrorStatusAndMessage("Stock not available");
				responseHelper.setReloadSectionIdWithUrl("error-details-container", "/SO19/error-details");
				return responseHelper.getResponse();
			}

			try {
				opdoheaderRepo.SO_ConfirmInvoice(sessionManager.getBusinessId(), sessionManager.getLoggedInUserDetails().getUsername(), opdoheader.getXdornum());
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("SO19")
						.xfunc("Confirm Invoice")
						.xsource("SO19")
						.xtable(null)
						.xdata(null)
						.xstatement("Confirm Invoice : SO_ConfirmInvoice(" + sessionManager.getBusinessId() +","+ sessionManager.getLoggedInUserDetails().getUsername() +","+ opdoheader.getXdornum() +")")
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xwh", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusim", xstatusim));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO19/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoices confirmed successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/invoice-delete")
	public @ResponseBody Map<String, Object> invoiceDelete(
		@RequestParam Integer xdornum,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) String xstatusim
		) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO19SearchParam param = new SO19SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXstatusim(xstatusim);

		Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), xdornum));
		if(!opdoheaderOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Invoice not found");
			return responseHelper.getResponse();
		}

		Opdoheader opdoheader = opdoheaderOp.get();
		if(!"Confirmed".equals(opdoheader.getXstatus())) {
			responseHelper.setErrorStatusAndMessage("Invoice status not confirmed");
			return responseHelper.getResponse();
		}

		if(!"Open".equals(opdoheader.getXstatusim())) {
			responseHelper.setErrorStatusAndMessage("Invoice inventory status not open");
			return responseHelper.getResponse();
		}

		try {
			opdodetailRepo.deleteAllByZidAndXdornum(sessionManager.getBusinessId(), xdornum);

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("SO19")
						.xfunc("Delete All Detail")
						.xsource("SO19")
						.xtable(null)
						.xdata(xdornum.toString())
						.xstatement("Delete All Detail Data : " + opdoheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);

			opdoheaderRepo.delete(opdoheader);

			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("SO19")
						.xfunc("Delete Data")
						.xsource("SO19")
						.xtable(null)
						.xdata(xdornum.toString())
						.xstatement("Delete Data : " + opdoheader.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xtype", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusim", xstatusim));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO19/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoice deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/delete-all")
	public @ResponseBody Map<String, Object> deleteAllInvoice(
		@RequestParam String selectedInvoices,
		@RequestParam String xfdate,	
		@RequestParam String xtdate,
		@RequestParam(required = false) Integer xbuid,
		@RequestParam(required = false) Integer xwh,
		@RequestParam(required = false) String xstatusim
		) {

		if(StringUtils.isBlank(selectedInvoices)) {
			responseHelper.setErrorStatusAndMessage("No invoice seleted");
			return responseHelper.getResponse();
		}

		List<String> invoices = Arrays.asList(selectedInvoices.split(","));
		if(invoices.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("No invoice seleted");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SO19SearchParam param = new SO19SearchParam();
		try {
			param.setXfdate(sdf.parse(xfdate));
			param.setXtdate(sdf.parse(xtdate));
		} catch (ParseException e) {
			log.error(ERROR, e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getCause().getMessage());
			return responseHelper.getResponse();
		}
		param.setXbuid(xbuid);
		param.setXwh(xwh);
		param.setXstatusim(xstatusim);

		List<Opdoheader> allValidInvoices = new ArrayList<>();

		for(String invoice : invoices) {
			Optional<Opdoheader> opdoheaderOp = opdoheaderRepo.findById(new OpdoheaderPK(sessionManager.getBusinessId(), Integer.parseInt(invoice)));
			if(opdoheaderOp.isPresent()) {
				Opdoheader opdoheader = opdoheaderOp.get();
				if("Confirmed".equals(opdoheader.getXstatus()) && "Open".equals(opdoheader.getXstatusim())) {
					allValidInvoices.add(opdoheader);
				}
			}
		}

		if(allValidInvoices.isEmpty()) {
			responseHelper.setErrorStatusAndMessage("You are not select any valid invoice");
			return responseHelper.getResponse();
		}

		for(Opdoheader opdoheader : allValidInvoices) {
			try {
				opdodetailRepo.deleteAllByZidAndXdornum(sessionManager.getBusinessId(), opdoheader.getXdornum());
				
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("SO19")
							.xfunc("Delete All Detail")
							.xsource("SO19")
							.xtable(null)
							.xdata(opdoheader.getXdornum().toString())
							.xstatement("Delete All Detail Data : " + opdoheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);

				opdoheaderRepo.delete(opdoheader);

				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("SO19")
							.xfunc("Delete Data")
							.xsource("SO19")
							.xtable(null)
							.xdata(opdoheader.getXdornum().toString())
							.xstatement("Delete Data : " + opdoheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			} catch (Exception e) {
				throw new IllegalStateException(e.getCause().getMessage());
			}

		}

		List<ReloadSectionParams> postData = new ArrayList<>();
		postData.add(new ReloadSectionParams("xfdate", xfdate));
		postData.add(new ReloadSectionParams("xtdate", xtdate));
		postData.add(new ReloadSectionParams("xtype", xwh != null ? xwh.toString() : ""));
		postData.add(new ReloadSectionParams("xbuid", xbuid != null ? xbuid.toString() : ""));
		postData.add(new ReloadSectionParams("xstatusim", xstatusim));

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("header-table-container", "/SO19/header-table", postData));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Invoices deleted successfully");
		return responseHelper.getResponse();
	}
}
