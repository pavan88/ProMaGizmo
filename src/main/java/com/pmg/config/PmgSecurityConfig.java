package com.pmg.config;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.pmg" })
public class PmgSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("loginService")
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^2222222222222222^^^^^^^^^^^^^");
		System.out.println("Returning the Login Page");
		// http.authorizeRequests().anyRequest().authenticated().and().formLogin().loginPage("/login").
				http.authorizeRequests().antMatchers("/login**").access("hasRole('ROLE_USER')")
				.and().formLogin()
				.loginPage("/login").failureUrl("/login?error").permitAll()
				.and().authorizeRequests().anyRequest().denyAll()
			
				.and().exceptionHandling().accessDeniedPage("/403");

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver() {
		return new AuthenticationTrustResolverImpl();
	}

}
