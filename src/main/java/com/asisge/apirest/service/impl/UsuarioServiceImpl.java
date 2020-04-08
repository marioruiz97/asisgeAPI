package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IAsesorService;
import com.asisge.apirest.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

	@Autowired
	private IUsuarioDao repository;

	@Autowired
	private IAsesorService asesorService;

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return repository.saveAndFlush(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAllUsuarios() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findUsuarioById(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> findUsuarioByCorreo(String correo) {
		return repository.findByCorreo(correo);		
	}

	@Override
	public void deleteUsuario(Long id) {
		asesorService.deleteByUsuario(id);
		repository.deleteById(id);
	}

	@Override
	public Usuario buildEntity(UsuarioDto dto) {
		TipoDocumento documento = new TipoDocumento();
		documento.setId(dto.getTipoDocumento());
		return new Usuario(null, dto.getIdentificacion(), dto.getNombre(), dto.getApellido1(), dto.getApellido2(),
				dto.getTelefono(), dto.getCorreo(), dto.getContrasena(), dto.getEstado(), documento, dto.getRoles());
	}

	@Override
	public Usuario changeEstadoUsuario(Long idUsuario, Boolean estado) {
		repository.updateEstadoUsuario(idUsuario, estado);
		return repository.findById(idUsuario).orElse(null);
	}

	/**
	 * implementacion de UserDetailService de Sring Security, se puede abstraer en
	 * otra clase despues
	 */

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		Usuario usuario = repository.findByCorreo(username).orElse(null);

		if (usuario == null) {
			throw new UsernameNotFoundException("No se ha encontrado el usuario en base de datos");
		}

		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(rol-> new SimpleGrantedAuthority(rol.getNombreRole()))
				.collect(Collectors.toList());
		return new User(username, usuario.getContrasena(), usuario.getEstado(), true, true, true, authorities);
	}

	
}
