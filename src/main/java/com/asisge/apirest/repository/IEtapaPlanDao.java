package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

public interface IEtapaPlanDao extends JpaRepository<EtapaPDT, Long> {

	List<EtapaPDT> findByPlanDeTrabajo(PlanDeTrabajo planDeTrabajo);
}
