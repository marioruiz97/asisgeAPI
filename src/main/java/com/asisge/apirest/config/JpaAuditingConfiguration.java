package com.asisge.apirest.config;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(2)
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public AuditorAware<String> auditorProvider() {
		/*
		 * SecurityHolderContext
		 */
		return () -> Optional.ofNullable("sistemas@asisge.com");
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
        http
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .cors()
                .configurationSource(corsConfigurationSource())
                .and()
                .httpBasic()
                .and().csrf().disable();
    }
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT", "PATCH" ,"DELETE", "OPTIONS"));        
        source.registerCorsConfiguration("/**", config);
        return source;              
    }
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}


}
