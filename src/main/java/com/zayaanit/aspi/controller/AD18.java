package com.zayaanit.aspi.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zayaanit.aspi.entity.Cadoc;
import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.entity.pk.CadocPK;
import com.zayaanit.aspi.entity.pk.XscreensPK;
import com.zayaanit.aspi.model.ReloadSection;
import com.zayaanit.aspi.repo.CadocRepo;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 * @since Aug 5, 2023
 */
@Slf4j
@Controller
@RequestMapping("/AD18")
public class AD18 extends KitController {
	private String pageTitle = null;
	private SimpleDateFormat sdfpath = new SimpleDateFormat("MMyyyy");

	@Autowired private CadocRepo cadocRepo;

	@Override
	protected String screenCode() {
		return "AD18";
	}

	@Override
	protected boolean isFavorite() {
		return checkTheScreenIsInFavouriteList(screenCode());
	}

	@Override
	protected String pageTitle() {
		if (this.pageTitle != null) return this.pageTitle;
		Optional<Xscreens> op = xscreenRepo.findById(new XscreensPK(sessionManager.getBusinessId(), "AD18"));
		if (!op.isPresent()) return null;
		this.pageTitle = op.get().getXtitle();
		return this.pageTitle;
	}

	@GetMapping("/download/{xdocid}")
	public ResponseEntity<Resource> download(@PathVariable Integer xdocid) {

		Optional<Cadoc> cadocOp = cadocRepo.findById(new CadocPK(sessionManager.getBusinessId(), xdocid));
		if (!cadocOp.isPresent()) return ResponseEntity.notFound().build();

		Cadoc cadoc = cadocOp.get();
		String filename = cadoc.getXname() + cadoc.getXext();

		// Create storage path
		String storage = storageLocation(cadoc.getXscreen(), cadoc.getXtrnnum().toString(), cadoc.getXdocid().toString(), cadoc.getXmmyyyy());
		Path filePath = Paths.get(storage).resolve(filename);
		Resource resource = new org.springframework.core.io.PathResource(filePath);
		if (!resource.exists()) {
			return ResponseEntity.notFound().build();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + cadoc.getXnameold());

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@Transactional
	@DeleteMapping("/delete/{xdocid}")
	public @ResponseBody Map<String, Object> delete(@PathVariable Integer xdocid, String mainreloadid, String mainreloadurl) {
		Optional<Cadoc> cadocOp = cadocRepo.findById(new CadocPK(sessionManager.getBusinessId(), xdocid));
		if (!cadocOp.isPresent()) {
			responseHelper.setErrorStatusAndMessage("Document not found in this system");
			return responseHelper.getResponse();
		}

		Cadoc cadoc = cadocOp.get();
		String filename = cadoc.getXname() + cadoc.getXext();

		// Create storage path
		String storage = storageLocation(cadoc.getXscreen(), cadoc.getXtrnnum().toString(), cadoc.getXdocid().toString(), cadoc.getXmmyyyy());
		Path filePath = Paths.get(storage).resolve(filename);

		// now delete files first
		if (!Files.exists(filePath)) {
			responseHelper.setErrorStatusAndMessage("Document not found in storage");
			return responseHelper.getResponse();
		}

		try {
			Files.delete(filePath);
		} catch (IOException e) {
			log.error("Error is : {}, {}", e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage("Can't delete document from storage");
			return responseHelper.getResponse();
		}

		try {
			cadocRepo.delete(cadoc);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection(mainreloadid, mainreloadurl + cadoc.getXtrnnum()));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Document deleted successfully");
		return responseHelper.getResponse();
	}

	@Transactional
	@PostMapping("/upload")
	public @ResponseBody Map<String, Object> upload(
			@RequestParam MultipartFile file,
			@RequestParam String screenId,
			@RequestParam String transactionId,
			@RequestParam String mainreloadid,
			@RequestParam String mainreloadurl,
			@RequestParam (required = false) String title,
			@RequestParam (required = false) String description
		) {

		if(file == null) {
			responseHelper.setErrorStatusAndMessage("File not found");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(screenId)) {
			responseHelper.setErrorStatusAndMessage("Screen id required");
			return responseHelper.getResponse();
		}

		if(StringUtils.isBlank(transactionId)) {
			responseHelper.setErrorStatusAndMessage("Transaction id required");
			return responseHelper.getResponse();
		}

		String docId = xscreenRepo.Fn_getTrn(sessionManager.getBusinessId(), "AD18").toString();
		UUID fileID = UUID.randomUUID();

		Cadoc cadoc = new Cadoc();
		cadoc.setXtitle(title);
		cadoc.setXdesc(description);
		cadoc.setZid(sessionManager.getBusinessId());
		cadoc.setXdocid(Integer.valueOf(docId));
		cadoc.setXscreen(screenId);
		cadoc.setXtrnnum(Integer.valueOf(transactionId));
		cadoc.setXnameold(file.getOriginalFilename());
		cadoc.setXname(fileID.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
		cadoc.setXmmyyyy(sdf.format(new Date()));

		String fileType = getFileExtention(file);

		String docTypes = sessionManager.getZbusiness().getXdoctypes();
		if(StringUtils.isBlank(docTypes)) {
			responseHelper.setErrorStatusAndMessage("File upload not supported");
			return responseHelper.getResponse();
		}
		if(!"na".equalsIgnoreCase(docTypes)) {
			String[] supportedExtentions = docTypes.trim().split(",");

			// CHeck file type
			if (!Arrays.asList(supportedExtentions).contains(fileType)) {
				responseHelper.setErrorStatusAndMessage("File type not supported");
				return responseHelper.getResponse();
			}
		}

		cadoc.setXext(fileType);

		// check file size
		if(file.getSize()  > sessionManager.getZbusiness().getXfilesize() * 1024) {
			responseHelper.setErrorStatusAndMessage("File size too big. Max file size supported : " + sessionManager.getZbusiness().getXfilesize() + "KB");
			return responseHelper.getResponse();
		}

		// Create storage path
		String storage = storageLocation(screenId, transactionId, docId, sdfpath.format(new Date())); 

		// Make Directory
		Path path = Paths.get(storage);
		if (!(Files.exists(path))) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				log.error("Error is {} , {}", e.getMessage(), e);
				responseHelper.setErrorStatusAndMessage("Can't create directory to upload file");
				return responseHelper.getResponse();
			}
		}

		// upload file
		try (InputStream inputStream = file.getInputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			int chunkNumber = 0;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				saveChunkToDisk(buffer, bytesRead, storage, cadoc.getXname() + cadoc.getXext(), chunkNumber);
				chunkNumber++;
			}
		} catch (IOException e) {
			log.error("Error is {}, {}", e.getMessage(), e);
			responseHelper.setErrorStatusAndMessage(e.getMessage());
			return responseHelper.getResponse();
		}

		try {
			cadoc = cadocRepo.save(cadoc);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause().getMessage());
		}

		List<ReloadSection> reloadSections = new ArrayList<>();
		reloadSections.add(new ReloadSection(mainreloadid, mainreloadurl + transactionId));
		responseHelper.setReloadSections(reloadSections);
		responseHelper.setSuccessStatusAndMessage("Document uploaded successfully");
		return responseHelper.getResponse();
	}

	private String storageLocation(String screenId, String transactionId, String docId, String xdate) {

		return new StringBuilder(StringUtils.isBlank(sessionManager.getZbusiness().getXdocpath()) ? "c:\\Contents" : sessionManager.getZbusiness().getXdocpath())
					.append(File.separator)
					.append(xdate)
					.append(File.separator)
					.append(screenId).toString();
	}

	private void saveChunkToDisk(byte[] chunk, int bytesRead, String directory, String fileName, int chunkNumber) throws IOException {
		Path targetPath = Paths.get(directory, fileName);
		Files.write(targetPath, chunk, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	private String getFileExtention(MultipartFile csvFile) {
		if (csvFile == null || StringUtils.isBlank(csvFile.getOriginalFilename()))
			return null;
		int indx =  csvFile.getOriginalFilename().lastIndexOf(".");
		return csvFile.getOriginalFilename().substring(indx);
	}

}
