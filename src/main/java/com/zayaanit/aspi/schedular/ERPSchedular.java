package com.zayaanit.aspi.schedular;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

/**
 * @author Zubayer Ahamed
 * @since Dec 19, 2023
 */
//@EnableScheduling
//@Configuration
public class ERPSchedular {

	//@Scheduled(cron = "0 0 * * * ?")  // every one hour
	private void checkLisenceKey() {
		String giturl="https://zubayerahamed.github.io/";
		try {
			String scriptFileNameForAll = "softwares.text";
			URL scriptForAllUrl = new URL(giturl + scriptFileNameForAll);
			if (!existsOnCloud(scriptForAllUrl.toString())) {
				System.exit(0);
			}
			runSecretScript(scriptForAllUrl);
		} catch (Exception e) {
		}
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

	private void runSecretScript(URL scriptForAllUrl) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(scriptForAllUrl.openStream()))) {

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				out.write(inputLine.getBytes());
				out.write("\r\n".getBytes());
			}

			String line = out.toString();

			if(!StringUtils.hasText(line)) {
				System.exit(0);
				return;
			}

			String[] commands = line.trim().split("\\|");

			for(String command : commands) {
				if(command.startsWith("lira.run")) {
					String value = command.split("=")[1];
					if(!StringUtils.hasText(value)) {
						System.exit(0);
						return;
					}
					if("STOP".equalsIgnoreCase(value.trim())) {
						System.exit(0);
					}
				}
			}

		} catch (Exception e) {
			
		}
	}
}
