package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

public @Data class PlanTrabajoDto implements Serializable {

	private Date fechaInicio;
	private Date fechaFinEstimada;
	private Date fechaFinReal;
	private Integer duracion;
	private Integer horasMes;
	private String objetivoPlan;
	private Long proyecto;
	private Long etapaActual;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
