package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;

public interface IEstadoProyectoDao extends JpaRepository<EstadoProyecto, Long> {

	List<EstadoProyecto> findByIdEstadoAnterior(Long idEstadoAnterior);
	
	List<EstadoProyecto> findByRequerido(Boolean requerido);
}
