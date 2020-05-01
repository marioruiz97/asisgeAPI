package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;

public interface ISeguimientoDao extends JpaRepository<Seguimiento, Long> {

	
	List<Seguimiento> findByActividadAsociada(Actividad actividadAsociada);
}
