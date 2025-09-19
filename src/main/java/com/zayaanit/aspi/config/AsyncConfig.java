package com.zayaanit.aspi.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

/**
 * @author Zubayer Ahamed
 * @since Sep 8, 2025
 */
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

	@Bean
	@Primary
	ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(1000);
		executor.setKeepAliveSeconds(60);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setVirtualThreads(true);
		executor.setThreadNamePrefix("async-");
		executor.initialize();
		return executor;
	}

	@Bean
	DelegatingSecurityContextAsyncTaskExecutor delegatingExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		return new DelegatingSecurityContextAsyncTaskExecutor(threadPoolTaskExecutor);
	}

	@Override
	public Executor getAsyncExecutor() {
		// 👇 this ensures ALL @Async methods use SecurityContext aware executor
		return delegatingExecutor(threadPoolTaskExecutor());
	}
}
