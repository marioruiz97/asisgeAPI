package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;

public interface IPlanDeTrabajoDao extends JpaRepository<PlanDeTrabajo, Long> {

	List<PlanDeTrabajo> findByProyecto(Proyecto proyecto);
	
}
