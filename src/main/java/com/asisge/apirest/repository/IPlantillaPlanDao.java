package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;

public interface IPlantillaPlanDao extends JpaRepository<PlantillaPlanTrabajo, Long> {

}
