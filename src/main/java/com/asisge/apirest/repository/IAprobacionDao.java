package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asisge.apirest.model.entity.proyectos.AprobacionPlan;

@Repository
public interface IAprobacionDao extends JpaRepository<AprobacionPlan, Long> {

}
