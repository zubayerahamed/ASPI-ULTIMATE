package com.zayaanit.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zayaanit.entity.Acdetail;
import com.zayaanit.entity.Acheader;
import com.zayaanit.entity.Acmst;
import com.zayaanit.entity.Acsub;
import com.zayaanit.entity.Cabunit;
import com.zayaanit.entity.Cadoc;
import com.zayaanit.entity.Xlogsdt;
import com.zayaanit.entity.Xscreens;
import com.zayaanit.entity.pk.AcdetailPK;
import com.zayaanit.entity.pk.AcheaderPK;
import com.zayaanit.entity.pk.AcmstPK;
import com.zayaanit.entity.pk.AcsubPK;
import com.zayaanit.entity.pk.CabunitPK;
import com.zayaanit.entity.pk.XscreensPK;
import com.zayaanit.model.XlogsdtEvent;
import com.zayaanit.repo.AcdetailRepo;
import com.zayaanit.repo.AcheaderRepo;
import com.zayaanit.repo.AcmstRepo;
import com.zayaanit.repo.AcsubRepo;
import com.zayaanit.repo.CabunitRepo;
import com.zayaanit.repo.CadocRepo;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Controller
@RequestMapping("/FA17")
public class FA17 extends KitController {

	@Autowired private AcheaderRepo acheaderRepo;
	@Autowired private CabunitRepo cabunitRepo;
	@Autowired private AcmstRepo acmstRepo;
	@Autowired private AcsubRepo acsubRepo;
	@Autowired private AcdetailRepo acdetailRepo;
	@Autowired private CadocRepo cadocRepo;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "FA17";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if(this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "FA17"));
		if(!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(@RequestParam (required = false) String xvoucher, @RequestParam(required = false) String frommenu, HttpServletRequest request, Model model) {
		if(isAjaxRequest(request) && frommenu == null) {
			if("RESET".equalsIgnoreCase(xvoucher)) {
				model.addAttribute("acheader", Acheader.getDefaultInstance());
				return "pages/FA17/FA17-fragments::main-form";
			}

			Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
			Acheader acheader = null;
			if(op.isPresent()) {
				acheader = op.get();

				if(acheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), acheader.getXbuid()));
					if(cabunitOp.isPresent()) acheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(acheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acheader.getXstaff()));
					if(acsubOp.isPresent()) acheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("acheader", acheader != null ? acheader : Acheader.getDefaultInstance());

			if(acheader != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("FA17")
							.xfunc("View Data")
							.xsource("FA17")
							.xtable(null)
							.xdata(acheader.getXvoucher().toString())
							.xstatement("View data : " + acheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}

			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA17", Integer.valueOf(xvoucher));
			model.addAttribute("documents", cdocList);

			return "pages/FA17/FA17-fragments::main-form";
		}

		if(frommenu == null) return "blank";

		if(isAjaxRequest(request) && StringUtils.isNotBlank(xvoucher) && !"RESET".equalsIgnoreCase(xvoucher)) {
			Optional<Acheader> op = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
			Acheader acheader = null;

			if(op.isPresent()) {
				acheader = op.get();

				if(acheader.getXbuid() != null) {
					Optional<Cabunit> cabunitOp = cabunitRepo.findById(new CabunitPK(sessionManager.getBusinessId(), acheader.getXbuid()));
					if(cabunitOp.isPresent()) acheader.setBusinessUnitName(cabunitOp.get().getXname());
				}

				if(acheader.getXstaff() != null) {
					Optional<Acsub> acsubOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), acheader.getXstaff()));
					if(acsubOp.isPresent()) acheader.setStaffName(acsubOp.get().getXname());
				}
			}
			model.addAttribute("acheader", acheader != null ? acheader : Acheader.getDefaultInstance());

			if(acheader != null) {
				eventPublisher.publishEvent(
						new XlogsdtEvent(
							Xlogsdt.builder()
							.xscreen("FA17")
							.xfunc("View Data")
							.xsource("FA17")
							.xtable(null)
							.xdata(acheader.getXvoucher().toString())
							.xstatement("View data : " + acheader.toString())
							.xresult("Success")
							.build(), 
							sessionManager
						)
					);
			}

			List<Cadoc> cdocList = cadocRepo.findAllByZidAndXscreenAndXtrnnum(sessionManager.getBusinessId(), "FA17", Integer.valueOf(xvoucher));
			model.addAttribute("documents", cdocList);

			// Details
			List<Acdetail> detailList = acdetailRepo.findAllByZidAndXvoucher(sessionManager.getBusinessId(), Integer.parseInt(xvoucher));
			for(Acdetail detail : detailList) {
				Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), detail.getXacc()));
				if(accountOp.isPresent()) {
					detail.setAccountName(accountOp.get().getXdesc());
					detail.setAccountUsage(accountOp.get().getXaccusage());
				}

				Optional<Acsub> subAccountOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), detail.getXsub()));
				if(subAccountOp.isPresent()) detail.setSubAccountName(subAccountOp.get().getXname());
			}
			model.addAttribute("detailList", detailList);

			model.addAttribute("acdetail", Acdetail.getDefaultInstance(Integer.parseInt(xvoucher)));

			return "pages/FA17/FA17";
		}

		model.addAttribute("acheader", Acheader.getDefaultInstance());
		return "pages/FA17/FA17";
	}

	@GetMapping("/detail-table")
	public String detailFormFragment(@RequestParam String xvoucher, @RequestParam String xrow, @RequestParam(required = false) Integer xacc, Model model) {
		if("RESET".equalsIgnoreCase(xvoucher) && "RESET".equalsIgnoreCase(xrow)) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA17/FA17-fragments::detail-table";
		}

		Optional<Acheader> oph = acheaderRepo.findById(new AcheaderPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher)));
		if(!oph.isPresent()) {
			model.addAttribute("acheader", Acheader.getDefaultInstance());
			return "pages/FA17/FA17-fragments::detail-table";
		}
		model.addAttribute("acheader", oph.get());

		List<Acdetail> detailList = acdetailRepo.findAllByZidAndXvoucher(sessionManager.getBusinessId(), Integer.parseInt(xvoucher));
		for(Acdetail detail : detailList) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), detail.getXacc()));
			if(accountOp.isPresent()) {
				detail.setAccountName(accountOp.get().getXdesc());
				detail.setAccountUsage(accountOp.get().getXaccusage());
			}

			Optional<Acsub> subAccountOp = acsubRepo.findById(new AcsubPK(sessionManager.getBusinessId(), detail.getXsub()));
			if(subAccountOp.isPresent()) detail.setSubAccountName(subAccountOp.get().getXname());
		}
		model.addAttribute("detailList", detailList);

		Acmst account = null;
		if(xacc != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), xacc));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}

		if("RESET".equalsIgnoreCase(xrow)) {
			Acdetail acdetail = Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
			if(account != null) {
				acdetail.setXacc(account.getXacc());
				acdetail.setAccountName(account.getXdesc());
				acdetail.setAccountUsage(account.getXaccusage());
			}

			model.addAttribute("acdetail", acdetail);
			return "pages/FA17/FA17-fragments::detail-table";
		}

		Optional<Acdetail> acdetailOp = acdetailRepo.findById(new AcdetailPK(sessionManager.getBusinessId(), Integer.parseInt(xvoucher), Integer.parseInt(xrow)));
		Acdetail acdetail = acdetailOp.isPresent() ? acdetailOp.get() : Acdetail.getDefaultInstance(Integer.parseInt(xvoucher));
		if(acdetail != null && acdetail.getXacc() != null) {
			Optional<Acmst> accountOp =  acmstRepo.findById(new AcmstPK(sessionManager.getBusinessId(), acdetail.getXacc()));
			account = accountOp.isPresent() ? accountOp.get() : null;
		}
		if(acdetail != null && account != null) {
			acdetail.setXacc(account.getXacc());
			acdetail.setAccountName(account.getXdesc());
			acdetail.setAccountUsage(account.getXaccusage());
		}

		if(acdetail != null && acdetail.getXrow() != 0) {
			eventPublisher.publishEvent(
					new XlogsdtEvent(
						Xlogsdt.builder()
						.xscreen("FA16")
						.xfunc("View Detail")
						.xsource("FA16")
						.xtable(null)
						.xdata(acdetail.getXvoucher().toString() + "/" + acdetail.getXrow())
						.xstatement("View detail data " + acdetail.toString())
						.xresult("Success")
						.build(), 
						sessionManager
					)
				);
		}

		model.addAttribute("acdetail", acdetail);
		return "pages/FA17/FA17-fragments::detail-table";
	}

	@GetMapping("/list-table")
	public String loadListTableFragment(Model model) {
		return "pages/FA17/FA17-fragments::list-table";
	}
}
