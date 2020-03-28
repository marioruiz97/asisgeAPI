package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

public interface IPlanDeTrabajoDao extends JpaRepository<PlanDeTrabajo, Long> {

}
