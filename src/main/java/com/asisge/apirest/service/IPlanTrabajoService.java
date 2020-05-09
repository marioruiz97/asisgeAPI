package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.proyectos.PlanTrabajoDto;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

public interface IPlanTrabajoService {

	PlanDeTrabajo savePlan(PlanDeTrabajo plan);

	PlanDeTrabajo createFromTemplate(PlanDeTrabajo plan, Long idPlantilla);

	List<PlanDeTrabajo> findAllPlanes();

	List<PlanDeTrabajo> findPlanesByProyecto(Long idProyecto);

	PlanDeTrabajo findPlanById(Long id);

	void deletePlan(Long id);

	PlanDeTrabajo buildPlanEntity(PlanTrabajoDto dto);

}
