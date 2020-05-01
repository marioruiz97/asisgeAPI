package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

public @Data class EtapaDto implements Serializable {

	Long planDeTrabajo;

	@NotBlank
	private String nombreEtapa;

	@NotNull
	private Date fechaInicio;

	@FutureOrPresent
	private Date fechaFin;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
