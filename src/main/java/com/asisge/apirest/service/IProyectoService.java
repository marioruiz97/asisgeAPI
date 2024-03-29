package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.proyectos.Dashboard;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.proyectos.Proyecto;

public interface IProyectoService {

	Proyecto saveProyecto(Proyecto proyecto);
	
	Proyecto findProyectoById(Long idProyecto);

	List<Proyecto> findAllProyectos();

	Dashboard loadDashboard(Long id);

	void deleteProyecto(Long id);

	Proyecto buildEntity(ProyectoDto dto);
	
	Integer calcAvance(List<Actividad> actividades);

}
