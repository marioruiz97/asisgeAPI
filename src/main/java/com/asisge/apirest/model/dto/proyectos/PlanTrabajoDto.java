package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.Data;

public @Data class PlanTrabajoDto implements Serializable {

	@NotNull
	private Date fechaInicio;
	
	@NotNull
	private Date fechaFinEstimada;
	
	
	private Date fechaFinReal;
	
	// private Integer duracion
	
	@PositiveOrZero
	private Integer horasMes;
	
	@NotBlank
	private String nombrePlan;
	
	@NotBlank
	private String objetivoPlan;
	
	private Long proyecto;
	private Long etapaActual;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
