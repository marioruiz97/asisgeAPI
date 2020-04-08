package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IAsesorService;
import com.asisge.apirest.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

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
	public void deleteUsuario(Long id) {
		asesorService.deleteByUsuario(id);
		repository.deleteById(id);
	}

	@Override
	public Usuario buildEntity(UsuarioDto dto) {
		TipoDocumento documento = new TipoDocumento();
		documento.setId(dto.getTipoDocumento());
		return new Usuario(null, dto.getIdentificacion(), dto.getNombre(), dto.getApellido1(), dto.getApellido2(),
				dto.getTelefono(), dto.getCorreo(), dto.getContrasena(), dto.getEstado(), documento);
	}

	@Override
	public Usuario changeEstadoUsuario(Long idUsuario, Boolean estado) {
		repository.updateEstadoUsuario(idUsuario, estado);
		return repository.findById(idUsuario).orElse(null);
	}

}
