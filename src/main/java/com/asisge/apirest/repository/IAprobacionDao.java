package com.asisge.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asisge.apirest.model.entity.proyectos.AprobacionPlan;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

@Repository
public interface IAprobacionDao extends JpaRepository<AprobacionPlan, Long> {

	Optional<AprobacionPlan> findByPlan(PlanDeTrabajo plan);
}
