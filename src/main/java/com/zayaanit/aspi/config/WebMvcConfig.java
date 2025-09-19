package com.zayaanit.aspi.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.zayaanit.aspi.entity.Xscreens;
import com.zayaanit.aspi.interceptor.MenuAccessAuthorizationInterceptor;
import com.zayaanit.aspi.interceptor.SessionTimeoutInterceptor;
import com.zayaanit.aspi.repo.XscreensRepo;

/**
 * @author Zubayer Ahamed
 * @since Jan 5, 2021
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private XscreensRepo xscreenRepo;
	@Autowired
	private SessionTimeoutInterceptor sessionTimeoutInterceptor;

	@Bean
	MenuAccessAuthorizationInterceptor menuAccessInterceptor() {
		return new MenuAccessAuthorizationInterceptor();
	}

	@Bean
	LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/messages", "classpath:/messages/messages-salesandinvoice");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Override
	public void addInterceptors(@NonNull InterceptorRegistry registry) {
		registry.addInterceptor(sessionTimeoutInterceptor).addPathPatterns("/**").excludePathPatterns("/login-assets/**", "/error", "/assets/**");
		registry.addInterceptor(menuAccessInterceptor()).addPathPatterns(getMenuPaths(true)).excludePathPatterns("/AD15/**");
		registry.addInterceptor(localeChangeInterceptor());
	}

	private String[] getMenuPaths(boolean forBusinessSelection) {
		List<Xscreens> list = xscreenRepo.findAll();
		list = list.stream().distinct().filter(l -> !l.getXtype().equalsIgnoreCase("System")).collect(Collectors.toList());

		List<String> paths = new ArrayList<>();
		for (Xscreens screen : list) {
			paths.add("/" + screen.getXscreen() + "/**");
		}

		if (forBusinessSelection) {
			paths.add("/");
			paths.add("/home/**");
			paths.add("/SA11/**");
			paths.add("/SA12/**");
			paths.add("/SA13/**");
			paths.add("/M01/**");
		}

		return paths.toArray(new String[paths.size()]);
	}

	
}
