package com.asisge.apirest.config.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IUsuarioService;

@Component
public class TokenInfo implements TokenEnhancer {

	static final String EMAIL_KEY = "email";
	static final String NAME_KEY = "name";
	static final String ENABLED_KEY = "enabled";
	static final String VALIDATED_KEY = "validated";
	static final String USER_ID_KEY = "userId";
	static final String USER_NAME_KEY = "user_name";
	static final String CLIENT_ID_KEY = "client_id";
	// private static final String PICTURE_URL = "picture_url"
	private static final String ROLES_KEY = "roles";

	@Autowired
	private IUsuarioService userService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		Map<String, Object> info = new HashMap<>();
		Usuario user = userService.findUsuarioByCorreo(authentication.getName()).orElse(null);
		
		if(user==null) {
			throw new UsernameNotFoundException("No se ha encontrado el usuario en base de datos");
		}

		info.put(EMAIL_KEY, user.getCorreo());
		info.put(NAME_KEY, user.getCorreo());
		info.put(ENABLED_KEY, user.getCorreo());
		info.put(ROLES_KEY, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

		info.put("auth", authentication.getName());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		return accessToken;
	}

}
