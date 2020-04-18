package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.Data;

public @Data class ProyectoDto implements Serializable {

	@NotBlank
	@Size(max = 50)
	private String nombreProyecto;

	private String descripcionGeneral;

	@FutureOrPresent
	@Nullable
	private Date fechaCierreProyecto;

	@NotNull
	Long estadoProyecto;

	@NotNull
	Long cliente;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
