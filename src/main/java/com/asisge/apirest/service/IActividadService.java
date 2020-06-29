package com.asisge.apirest.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.asisge.apirest.model.dto.actividades.ActividadDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

public interface IActividadService {

	Actividad saveActividad(Actividad actividad);

	void setResponsables(Actividad actividad, List<Long> usuarios);

	List<Actividad> findAllActividades();

	List<Actividad> findActividadesByUsuario(String email);

	List<Actividad> findActividadesByPlan(PlanDeTrabajo plan);

	List<Actividad> findActividadesByEtapa(Long idEtapa);

	Actividad findActividadById(Long idActividad);

	Actividad changeEstadoActividad(HttpServletRequest request, Actividad actividad, Long idEstado);

	void solicitarAprobacion(Actividad actividad);

	void notificarNoAprobada(Actividad actividad);

	void notificarCompletada(Actividad actividad);

	void deleteActividad(Long idActividad);

	Actividad buildActividad(ActividadDto dto);
}
