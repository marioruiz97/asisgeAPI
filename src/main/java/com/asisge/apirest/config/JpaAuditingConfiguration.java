package com.asisge.apirest.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

	@Bean
	public AuditorAware<String> auditorProvider() {
		/*
		 * SecurityContextHolder
		 */
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
			      .map(SecurityContext::getAuthentication)
			      .filter(Authentication::isAuthenticated)
			      .map(Authentication::getName);			      
	}

}
