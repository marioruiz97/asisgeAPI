package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;

public interface IEstadoProyectoService {

	EstadoProyecto saveEstado(EstadoProyecto estadoProyecto);

	List<EstadoProyecto> findAllEstados();

	EstadoProyecto findEstadoById(Long id);
	
	List<EstadoProyecto> findNextEstados(Long estadoActual);

	void deleteEstado(Long id);
}
