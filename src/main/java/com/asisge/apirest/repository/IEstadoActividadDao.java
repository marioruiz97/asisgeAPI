package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.EstadoActividad;

public interface IEstadoActividadDao extends JpaRepository<EstadoActividad, Long> {

}
