package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;

import lombok.Data;

public @Data class AprobacionPlanDto implements Serializable {

	private Boolean avalCliente;

	private String observaciones;

	private String rutaArchivo;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
