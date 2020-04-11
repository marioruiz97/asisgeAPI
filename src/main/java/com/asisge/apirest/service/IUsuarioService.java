package com.asisge.apirest.service;

import java.util.List;
import java.util.Optional;

import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IUsuarioService {
	
	Usuario saveUsuario(Usuario usuario);

	List<Usuario> findAllUsuarios();

	Usuario findUsuarioById(Long id);
	
	Optional<Usuario> findUsuarioByCorreo(String correo);

	void deleteUsuario(Long id);

	Usuario buildEntity(UsuarioDto dto);

	Usuario changeEstadoUsuario(Long idUsuario, Boolean estado);
	
	void changeContrasena(Usuario usuario);

}
