package com.zayaanit.aspi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.exceptions.UnauthorizedException;
import com.zayaanit.aspi.model.DBBackup;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.service.impl.DBBackupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Jul 3, 2023
 */
@Slf4j
@Controller
@RequestMapping("/SA16")
public class SA16 extends KitController {

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private DBBackupService backupService;

	private String pageTitle = null;

	@Override
	protected String screenCode() {
		return "SA16";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if (this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "SA16"));
		if (!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping
	public String index(HttpServletRequest request, Model model) throws UnauthorizedException {
		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			throw new UnauthorizedException("Unauthorize Access");
		}

		DBBackup db = new DBBackup();
		db.setXdate(new Date());
		model.addAttribute("db", db);

		// get all the list of files
		List<String> filesName = new ArrayList<String>();
		Path directory = Paths.get(backupLocation());
		try {
			// Get all files in the directory
			Files.walk(directory).filter(Files::isRegularFile).forEach(file -> {
				String fileName = file.getFileName().toString();
				if (fileName.endsWith(".bak")) {
					filesName.add(fileName);
				}
			});
		} catch (IOException e) {
			log.error(ERROR, e.getMessage(), e);
		}
		model.addAttribute("backups", filesName);

		if (isAjaxRequest(request)) {
			return "pages/SA16/SA16-fragments::main-form";
		}

		return "pages/SA16/SA16";
	}

	@Transactional
	@PostMapping("/store")
	public @ResponseBody Map<String, Object> store(DBBackup db) {

		if(!sessionManager.getLoggedInUserDetails().isAdmin()) {
			responseHelper.setErrorStatusAndMessage("Unauthorized Access");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(db.getXorg())) {
			responseHelper.setErrorStatusAndMessage("File Name Required");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd HHmmss");
		String fileName = db.getXorg().concat(sdf.format(new Date())).concat(".bak");

		// create path if not exist
		Path directoryPath = Paths.get(backupLocation());
		if (!Files.exists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				log.error(ERROR, e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
		}

		try {
			int stat = backupService.performBackup(appConfig.getDatabaseName(), backupLocation() + "/" + fileName);
			if(stat == 0) {
				responseHelper.setErrorStatusAndMessage("Backup failed");
				return responseHelper.getResponse();
			}
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		// Set up the response for download
		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection("main-form-container", "/SA16"));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Backup successfull");
		return responseHelper.getResponse();
	}

	@GetMapping("/download")
	@ResponseBody
	public void downloadBackupFile(HttpServletResponse response, @RequestParam("filename") String filename) throws IOException {

		File file = new File(backupLocation() + File.separator + filename);

		if (file.exists()) {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);

			try (FileInputStream fis = new FileInputStream(file); OutputStream out = response.getOutputStream()) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().println("Backup file not found.");
		}
	}

	@DeleteMapping("/delete")
	public @ResponseBody Map<String, Object> delete(@RequestParam("filename") String filename){
		File file = new File(backupLocation() + File.separator + filename);

		if (file.exists()) {
			// Attempt to delete the file
			if (file.delete()) {
				List<ReloadSection> reloadSections = new ArrayList<>();
				reloadSections.add(new ReloadSection("main-form-container", "/SA16"));
				responseHelper.setReloadSections(reloadSections);
				responseHelper.setSuccessStatusAndMessage("Backup Deleted Successfully");
				return responseHelper.getResponse();
			} else {
				responseHelper.setErrorStatusAndMessage("Failed to delete file");
				return responseHelper.getResponse();
			}
		} else {
			responseHelper.setErrorStatusAndMessage("File doesn't exist!");
			return responseHelper.getResponse();
		}
	}

	private String backupLocation() {
		String backupLocation = appConfig.getBackupLocation();
		if (backupLocation.endsWith("\\")) {
			backupLocation = backupLocation.substring(0, backupLocation.length() - 1);
		}
		backupLocation = backupLocation.trim();
		return backupLocation;
	}
}