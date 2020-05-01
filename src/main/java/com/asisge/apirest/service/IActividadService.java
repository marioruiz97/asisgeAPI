package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.actividades.ActividadDto;
import com.asisge.apirest.model.entity.actividades.Actividad;

public interface IActividadService {

	Actividad saveActividad(Actividad actividad);

	List<Actividad> findAllActividades();

	List<Actividad> findActividadesByUsuario(String email);

	List<Actividad> findActividadesByEtapa(Long idEtapa);

	// TODO que parametros debe recibir
	// List<Actividad> actividadesVencimientoProximo()

	Actividad findActividadById(Long idActividad);

	void deleteActividad(Long idActividad);
	
	Actividad buildActividad(ActividadDto dto);
}
