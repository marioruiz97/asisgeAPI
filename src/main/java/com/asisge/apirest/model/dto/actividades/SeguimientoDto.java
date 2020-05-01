package com.asisge.apirest.model.dto.actividades;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.Data;

public @Data class SeguimientoDto implements Serializable {

	@NotNull
	private Actividad actividadAsociada;
	
	@NotNull
	@Positive	
	private Integer horasTrabajadas;
		
	private Usuario usuarioSeguimiento;
	
	@NotBlank
	private String observaciones;
	
	@NotBlank
	private String descripcionLabor;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
