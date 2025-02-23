package com.zayaanit.aspi.service.impl;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Service;

import com.zayaanit.aspi.model.AsyncCSVResult;
import com.zayaanit.aspi.service.ImportExportService;

/**
 * @author Zubayer Ahamed
 * @since Feb 8, 2025
 */
@Service
@EnableAsync
public class AsyncCSVProcessor {

	@Bean 
	public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor delegate) { 
		return new DelegatingSecurityContextAsyncTaskExecutor(delegate); 
	}

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);  // Reduce core threads to save memory
		executor.setMaxPoolSize(20);  // Limit max threads
		executor.setQueueCapacity(10); // Reduce queue size to prevent memory overload
		executor.setKeepAliveSeconds(60); // Threads will be removed if idle for 60s
		executor.setAllowCoreThreadTimeOut(true);
		executor.setThreadNamePrefix("async-");
		return executor;
	}

	@Async
	public void processDataFromExcel(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.processDataFromExcel(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void processDataFromCSV(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.processCSV(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void confirmImportData(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.confirmImportData(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void importDataFromCSV(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) {
		importExportService.importCSV(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}

	@Async
	public void exportData(AsyncCSVResult asyncCSVResult, ImportExportService importExportService) throws IOException {
		importExportService.retreiveData(asyncCSVResult);
		asyncCSVResult.getLatch().countDown();
	}
}
