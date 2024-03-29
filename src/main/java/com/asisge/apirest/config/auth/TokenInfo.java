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

import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IUsuarioService;

@Component
public class TokenInfo implements TokenEnhancer {

	private static final String EMAIL_KEY = "usuario_email";		
	private static final String ENABLED_KEY = "usuario_enabled";			
	private static final String USER_ID_KEY = "usuario_id";
	private static final String USER_NAME_KEY = "usuario_name";
	private static final String ROLES_KEY = "usuario_roles";

	@Autowired
	private IUsuarioService userService;

	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		Usuario user = userService.findUsuarioByCorreo(authentication.getName());
		if (user == null) {
			String message = Messages.getString("message.username-not-found");
			throw new UsernameNotFoundException(message);
		}
		info.put(EMAIL_KEY, user.getCorreo());
		info.put(USER_NAME_KEY, user.getNombre().concat(" ").concat(user.getApellido1()));
		info.put(ENABLED_KEY, user.getEstado());
		info.put(USER_ID_KEY, user.getIdUsuario());
		info.put(ROLES_KEY, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
