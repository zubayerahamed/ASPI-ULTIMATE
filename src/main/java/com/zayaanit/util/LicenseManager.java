package com.zayaanit.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zubayer Ahamed
 * @since Dec 14, 2025
 */
public class LicenseManager {

	public static class LicenseData {
		private Map<String, String> properties;
		private Date expiryDate;
		private String licenseKey;

		public LicenseData(Map<String, String> properties, Date expiryDate, String licenseKey) {
			this.properties = properties;
			this.expiryDate = expiryDate;
			this.licenseKey = licenseKey;
		}

		public Map<String, String> getProperties() {
			return properties;
		}

		public Date getExpiryDate() {
			return expiryDate;
		}

		public String getLicenseKey() {
			return licenseKey;
		}
	}

	// Generate license key (SIMPLIFIED - No encryption during generation)
	public static LicenseData generateLicense(Map<String, String> properties, Date expiryDate, String secretKey)
			throws Exception {

		// Create a simple encoded string without complex encryption
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		StringBuilder sb = new StringBuilder();

		// Add properties in consistent order
		List<String> keys = new ArrayList<>(properties.keySet());
		Collections.sort(keys);

		for (String key : keys) {
			sb.append(key).append("=").append(properties.get(key)).append("|");
		}

		// Add expiry
		sb.append("EXPIRY=").append(sdf.format(expiryDate));

		String data = sb.toString();

		// Create a simple hash signature (not full encryption)
		String signature = createSignature(data, secretKey);

		// Combine data and signature
		String licenseString = data + "|SIG=" + signature;

		// Encode to Base64 for cleaner format
		String encoded = Base64.getEncoder().encodeToString(licenseString.getBytes());

		// Format nicely
		String formattedKey = formatKey(encoded);

		return new LicenseData(properties, expiryDate, formattedKey);
	}

	// Validate license (SIMPLIFIED - No complex decryption)
	public static Map<String, String> validateLicense(String licenseKey, String secretKey) throws Exception {

		// Clean and decode
		String cleanKey = licenseKey.replaceAll("[^a-zA-Z0-9+/=]", "");
		byte[] decodedBytes = Base64.getDecoder().decode(cleanKey);
		String licenseString = new String(decodedBytes);

		// Split into data and signature
		String[] parts = licenseString.split("\\|SIG=");
		if (parts.length != 2) {
			throw new SecurityException("Invalid license format");
		}

		String data = parts[0];
		String receivedSignature = parts[1];

		// Verify signature
		String calculatedSignature = createSignature(data, secretKey);
		if (!receivedSignature.equals(calculatedSignature)) {
			throw new SecurityException("License signature invalid");
		}

		// Parse the data
		Map<String, String> result = parseData(data);

		// Check expiry
		if (result.containsKey("EXPIRY")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date expiryDate = sdf.parse(result.get("EXPIRY"));
			Date currentDate = new Date();

			if (currentDate.after(expiryDate)) {
				result.put("STATUS", "EXPIRED");
			} else {
				result.put("STATUS", "VALID");
			}

			// Format for display
			SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd");
			result.put("EXPIRY_DATE", displayFormat.format(expiryDate));
		}

		return result;
	}

	// Simple signature creation (HMAC-like)
	private static String createSignature(String data, String secretKey) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		String input = data + secretKey;
		byte[] hash = md.digest(input.getBytes());

		// Take first 8 bytes for a shorter signature
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString().toUpperCase();
	}

	private static Map<String, String> parseData(String data) {
		Map<String, String> map = new HashMap<>();
		String[] pairs = data.split("\\|");

		for (String pair : pairs) {
			String[] keyValue = pair.split("=", 2);
			if (keyValue.length == 2) {
				map.put(keyValue[0], keyValue[1]);
			}
		}
		return map;
	}

	private static String formatKey(String base64) {
		// Remove padding for cleaner format
		String clean = base64.replace("=", "");

//		StringBuilder formatted = new StringBuilder();
//		for (int i = 0; i < clean.length(); i++) {
//			if (i > 0 && i % 4 == 0) {
//				formatted.append("-");
//			}
//			if (i % 16 == 0 && i > 0) {
//				formatted.append("\n");
//			}
//			formatted.append(clean.charAt(i));
//		}
//
//		return formatted.toString();
		return clean;
	}

	// Helper method to add days
	public static Date addDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}
}
