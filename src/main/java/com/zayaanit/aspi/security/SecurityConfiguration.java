package com.zayaanit.aspi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired private UserDetailsService userDetailsService;

	private static final String[] ALLOWED_URLS = new String[]{
			"/developer/**",
			"/login/fakelogin",
			"/login/directloginfragment",
			"/login/directloginfragment/**",
			"/login-assets/**",
			"/clearlogincache",
			"/api/**",
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/favicon.ico",
			"/configuration/ui",
			"/webjars/**",
			"/configuration/security",
			"/swagger-resources/**",
			"/v2/api-docs/**",
			"/v2/rest/**"
		};

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(authProvider())
				.build();
	}

	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http
			.addFilterBefore(authenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(ALLOWED_URLS).permitAll()
				.requestMatchers("/SA11/**", "/SA12/**", "/SA13/**", "/SA15/**", "/SA16/**").hasRole("ZADMIN")
				.anyRequest().authenticated()
			)
			.authenticationProvider(authProvider())
			.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.failureUrl("/login?error")
				.permitAll()
				.defaultSuccessUrl("/", true)
			)
			.logout(logout -> logout 
				.addLogoutHandler(logoutHandler())
				.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID_ASPI")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login?logout")
				.logoutSuccessHandler(logoutSuccessHandler())
				.permitAll()
			)
			.exceptionHandling(exc -> exc.accessDeniedPage("/accessdenied"));

		return http.build();
	}

	@Bean
	public LogoutHandler logoutHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	private SimpleAuthenticationFilter authenticationFilter(AuthenticationManager authenticationManager) throws Exception {
		SimpleAuthenticationFilter filter = new SimpleAuthenticationFilter(authenticationManager);
		filter.setAuthenticationFailureHandler(authenticationFailureHandler());
		filter.setSecurityContextRepository(new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()));
		return filter;
	}

	private AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}
