package com.asisge.apirest.config.auth;

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.asisge.apirest.model.entity.terceros.Usuario;


public class AsisgeAuthenticationManager implements AuthenticationManager {

	@Autowired UserDetailsService userService;


	 @Override
	    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	        UserDetails userDetails;
	        try {
	            userDetails = userService.loadUserByUsername(authentication.getName());
	        } catch (UsernameNotFoundException ex) {
	            userDetails = tryToRegister((TokenInfo) authentication.getDetails());
	        }

	        return new TechnovationSlpAuthenticationToken(authentication, userDetails.getAuthorities(), true);
	    }

	    private UserDetails tryToRegister(TokenInfo details) {

	        Usuario user = Usuario.builder()
	                .idUsuario(null)
	                .nombre(details.getName())
	                .
	                
	                
	                .name(details.getName())
	                .preferredEmail(details.getEmail())
	                .pictureUrl(details.getPictureUrl())
	                .enabled(false)
	                .validated(false)
	                .build();

	        FirebaseUser firebaseUser = FirebaseUser.builder()
	                .uid(details.getUid())
	                .email(details.getEmail())
	                .user(user)
	                .build();

	        return userService.register(firebaseUser);
	    }
}

*/