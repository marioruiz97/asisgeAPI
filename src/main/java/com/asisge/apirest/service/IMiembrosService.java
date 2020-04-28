package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.proyectos.MiembroDto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IMiembrosService {

	MiembroProyecto saveMiembro(MiembroProyecto miembro);

	@Deprecated
	List<MiembroProyecto> saveAll(List<MiembroDto> dtos);

	List<Proyecto> findProyectosByUsuario(Long idUsuario);

	List<Proyecto> findProyectosByEmail(String email);

	List<Usuario> findUsuariosByProyecto(Long idProyecto);

	List<MiembroProyecto> findMiembrosProyecto(Long idProyecto);

	List<Usuario> findPosiblesMiembros(Long idProyecto);

	boolean existsMiembroInProyecto(Long idProyecto, String email);

	void deleteMiembro(Long idMiembro);

	MiembroProyecto buildEntity(MiembroDto dto);

}
