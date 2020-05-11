package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.dto.actividades.EstadoActividadDto;
import com.asisge.apirest.model.entity.actividades.EstadoActividad;
import com.asisge.apirest.repository.IEstadoActividadDao;
import com.asisge.apirest.service.IEstadoActividadService;

@Service
public class EstadoActividadServiceImpl implements IEstadoActividadService {

	@Autowired
	private IEstadoActividadDao repository;

	@Override
	public EstadoActividad saveEstado(EstadoActividad estado) {
		return repository.save(estado);
	}

	@Override
	public List<EstadoActividad> findAll() {
		return repository.findAll();
	}

	@Override
	public EstadoActividad findEstadoById(Long idEstado) {
		return repository.findById(idEstado).orElse(null);
	}

	@Override
	public void deleteEstado(Long idEstado) {
		repository.deleteById(idEstado);
	}

	@Override
	public EstadoActividad buildEstado(EstadoActividadDto dto) {
		return new EstadoActividad(null, dto.getNombreEstado(), dto.getDescripcion(), dto.getEstadoInicial(),
				dto.getActividadNoAprobada(), dto.getActividadCompletada());
	}

}
