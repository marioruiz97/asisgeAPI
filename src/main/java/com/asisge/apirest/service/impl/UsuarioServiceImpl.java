package com.asisge.apirest.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.repository.IVerificationTokenDao;
import com.asisge.apirest.service.IAsesorService;
import com.asisge.apirest.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService, UserDetailsService {

	@Autowired
	private IUsuarioDao repository;

	@Autowired
	private IAsesorService asesorService;
	
	@Autowired
	private IVerificationTokenDao tokenDao;
	
	private BCryptPasswordEncoder encoder;
	
	@PostConstruct
	private void setEncoder() {
		encoder = new BCryptPasswordEncoder();
	}
	

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
	public Usuario findUsuarioByCorreo(String correo) {
		return repository.findByCorreoIgnoreCase(correo).orElse(null);		
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
		boolean verificado = false;
		String password = dto.getContrasena().length() < 15 ? encoder.encode(dto.getContrasena()) : dto.getContrasena();
		return new Usuario(null, dto.getIdentificacion(), dto.getNombre(), dto.getApellido1(), dto.getApellido2(),
				dto.getTelefono(), dto.getCorreo().toLowerCase(), password, dto.getEstado(), verificado, documento, dto.getRoles());
	}

	@Override
	public Usuario changeEstadoUsuario(Long idUsuario, Boolean estado) {
		repository.updateEstadoUsuario(idUsuario, estado);
		return repository.findById(idUsuario).orElse(null);
	}
	

	@Override
	public void changeContrasena(Usuario u) {
		u.setContrasena(encoder.encode(u.getContrasena()));
		repository.changeContrasenaUsuario(u.getIdUsuario(), u.getContrasena());
	}
	
	@Override
	public VerificationToken createVerificationToken(Usuario usuario) {		
		return tokenDao.save(new VerificationToken(usuario));
	}
	
	@Override
	public VerificationToken validVerificationToken(Usuario usuario) {
		VerificationToken token = tokenDao.findByUsuario(usuario).orElse(null);
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);		
		Date yesterday = cal.getTime();
		if(token != null && yesterday.compareTo(token.getCreatedDate()) < 0) {
			tokenDao.deleteById(token.getTokenId());
		}
		return createVerificationToken(usuario);
	}


	@Override
	public VerificationToken getVerificationToken(String token) {
		return tokenDao.findByToken(token).orElse(null);
	}

	/**
	 * implementacion de UserDetailService de Sring Security, se puede abstraer en
	 * otra clase despues
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		Usuario usuario = repository.findByCorreoIgnoreCase(username).orElse(null);

		if (usuario == null) {
			throw new UsernameNotFoundException("No se ha encontrado el usuario en base de datos");
		}

		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(rol-> new SimpleGrantedAuthority(rol.getNombreRole()))
				.collect(Collectors.toList());
		return new User(username, usuario.getContrasena(), usuario.getEstado(), true, true, usuario.getVerificado(), authorities);
	}

	
}
