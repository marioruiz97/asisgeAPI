package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.actividades.EstadoActividadDto;
import com.asisge.apirest.model.entity.actividades.EstadoActividad;

public interface IEstadoActividadService {

	EstadoActividad saveEstado(EstadoActividad estado);

	List<EstadoActividad> findAll();

	EstadoActividad findEstadoById(Long idEstado);

	void deleteEstado(Long idEstado);

	EstadoActividad buildEstado(EstadoActividadDto dto);
}
