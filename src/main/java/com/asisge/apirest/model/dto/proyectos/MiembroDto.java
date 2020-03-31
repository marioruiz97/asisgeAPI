package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public @Data class MiembroDto implements Serializable {
	
	@NotNull
	private Long usuario;
	@NotNull
	private Long proyecto;
	@NotBlank
	private String rolProyecto;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
