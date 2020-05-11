package com.asisge.apirest.model.dto.actividades;

import javax.validation.constraints.NotBlank;

import lombok.Data;

public @Data class EstadoActividadDto {

	@NotBlank
	private String nombreEstado;
	private String descripcion;
	private Boolean estadoInicial;
	private Boolean actividadNoAprobada;
	private Boolean actividadCompletada;
}
