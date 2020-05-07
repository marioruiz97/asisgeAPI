package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.List;

import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class PlanTrabajoBoard implements Serializable {

	private Long idPlanDeTrabajo;
	private PlanDeTrabajo planDeTrabajo;
	private List<EtapaBoard> etapas;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
