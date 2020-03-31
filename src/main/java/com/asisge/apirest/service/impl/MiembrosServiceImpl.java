package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.proyectos.MiembroDto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IMiembroProyectoDao;
import com.asisge.apirest.service.IMiembrosService;

@Service
public class MiembrosServiceImpl implements IMiembrosService {

	@Autowired
	private IMiembroProyectoDao repository;

	@Override
	public MiembroProyecto saveMiembro(MiembroProyecto miembro) {
		return repository.saveAndFlush(miembro);
	}

	@Override
	@Transactional
	public List<MiembroProyecto> saveAll(List<MiembroDto> dtos) {
		Set<MiembroProyecto> setMiembros = dtos.stream().map(this::buildEntity).collect(Collectors.toSet());
		List<MiembroProyecto> persist = setMiembros.stream().collect(Collectors.toList());
		repository.deleteAll(findMiembrosProyecto(dtos.get(0).getProyecto()));
		return repository.saveAll(persist);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiembroProyecto> findAllMiembros() {
		return repository.findAll(Sort.by(Direction.ASC, "proyecto", "usuario"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findProyectosByUsuario(Long idUsuario) {
		return repository.findProyectosUsuario(idUsuario);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findUsuariosByProyecto(Long idProyecto) {
		return repository.findUsuariosProyecto(idProyecto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MiembroProyecto> findMiembrosProyecto(Long idProyecto) {
		Proyecto p = new Proyecto();
		p.setIdProyecto(idProyecto);
		return repository.findByProyecto(p);
	}

	@Override
	public MiembroProyecto buildEntity(MiembroDto dto) {
		Proyecto proyecto = new Proyecto();
		proyecto.setIdProyecto(dto.getProyecto());
		return new MiembroProyecto(null, new Usuario(dto.getUsuario()), proyecto, dto.getRolProyecto());
	}

	@Override
	public void deleteMiembro(Long idProyecto, Long idUsuario) {
		int result = repository.deleteMiembro(idProyecto, idUsuario);
		if (result != 1)
			throw new RuntimeException();
	}

}
