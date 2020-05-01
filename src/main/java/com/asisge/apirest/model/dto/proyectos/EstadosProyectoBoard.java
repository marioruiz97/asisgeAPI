package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;

import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class EstadosProyectoBoard implements Serializable {

	private EstadoProyecto estado;
	private Boolean actual;
	private Boolean completado;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
