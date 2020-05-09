package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;

import lombok.Data;

public @Data class PlantillaPlanDto implements Serializable {

	@NotNull
	private @Valid PlantillaPlanTrabajo plantilla;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
