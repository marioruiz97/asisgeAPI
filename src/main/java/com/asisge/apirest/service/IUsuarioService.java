package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IUsuarioService {

	Usuario saveUsuario(Usuario usuario);

	List<Usuario> findAllUsuarios();

	Usuario findUsuarioById(Long id);

	Usuario findUsuarioByCorreo(String correo);

	void deleteUsuario(Long id);

	Usuario buildEntity(UsuarioDto dto);

	Usuario changeEstadoUsuario(Long idUsuario, Boolean estado);

	void changeContrasena(Usuario usuario);

	VerificationToken createVerificationToken(Usuario usuario);
	
	VerificationToken getVerificationToken(String token);
	
	VerificationToken validVerificationToken(Usuario usuario);
}
