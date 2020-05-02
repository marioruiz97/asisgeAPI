package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.dto.actividades.SeguimientoDto;

public interface ISeguimientoService {

	Seguimiento saveSeguimiento(Seguimiento seguimiento);

	List<Seguimiento> findAllSeguimientos();

	Seguimiento findSeguimientoById(Long idSeguimiento);

	List<Seguimiento> findByActividad(Long idActividad);

	void deleteSeguimiento(Long idSeguimiento);

	Seguimiento buildSeguimiento(SeguimientoDto dto);
}
