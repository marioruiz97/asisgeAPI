package com.asisge.apirest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.EstadoActividad;

public interface IEstadoActividadDao extends JpaRepository<EstadoActividad, Long> {

	Optional<EstadoActividad> findByEstadoInicial(Boolean estadoInicial);
}
