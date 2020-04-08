package com.asisge.apirest.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

	// TODO se debe validar si esta clase debe quitarse o cambiarse,

	@Bean
	public AuditorAware<String> auditorProvider() {
		/*
		 * SecurityHolderContext
		 */
		return () -> Optional.ofNullable("sistemas@asisge.com");
	}

}
