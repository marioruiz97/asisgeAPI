package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface ISeguimientoDao extends JpaRepository<Seguimiento, Long> {

	List<Seguimiento> findByActividadAsociadaOrderByIdSeguimientoAsc(Actividad actividadAsociada);

	List<Seguimiento> findByActividadAsociadaAndUsuarioSeguimiento(Actividad actividadAsociada, Usuario usuarioSeguimiento);
}
