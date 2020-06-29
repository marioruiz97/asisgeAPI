package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public @Data class CierreDto implements Serializable {

	@NotBlank
	private String observaciones;

	@NotNull
	private Boolean avalCliente;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
