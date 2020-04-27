package com.asisge.apirest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.proyectos.EstadoProyectoLineDto;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.repository.IEstadoProyectoDao;
import com.asisge.apirest.service.IEstadoProyectoService;

@Service
public class EstadoProyectoServiceImpl implements IEstadoProyectoService {

	@Autowired
	private IEstadoProyectoDao repository;

	private boolean validEstadoAnterior(Long idAnterior) {
		boolean result = false;
		if (repository.findByIdEstadoAnterior(null).isEmpty() && idAnterior == null)
			result = true;
		if (idAnterior != null && repository.existsById(idAnterior))
			result = true;
		return result;
	}

	@Override
	public EstadoProyecto saveEstado(EstadoProyecto estado) {
		if (!validEstadoAnterior(estado.getIdEstadoAnterior())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Messages.getString("message.error.create.estado-proyecto"));
		}
		return repository.save(estado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstadoProyecto> findAllEstados() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public EstadoProyecto findEstadoById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public void deleteEstado(Long id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstadoProyecto> findNextEstados(Long estadoActual) {
		return repository.findByIdEstadoAnterior(estadoActual);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstadoProyectoLineDto> findEstadosLine(EstadoProyecto actual) {
		List<EstadoProyecto> requeridos = repository.findByRequerido(Boolean.TRUE);
		List<EstadoProyectoLineDto> estadosLine = new ArrayList<>();
		EstadoProyecto cero = requeridos.stream().filter(estado -> estado.getIdEstadoAnterior() == null).findFirst().orElse(actual);
		requeridos.remove(cero);
		boolean completadoFlag = true;
		
		if (actual.equals(cero)) {
			estadosLine.add(new EstadoProyectoLineDto(cero, true, true));
			completadoFlag = false;
		} else {
			estadosLine.add(new EstadoProyectoLineDto(cero, false, true));
		}
		if (actual.getRequerido().booleanValue()) {			
			EstadoProyecto siguiente = requeridos.stream()
					.filter(estado -> estado.getIdEstadoAnterior().equals(cero.getId()))
					.findFirst().orElse(null);			
			while (siguiente != null) {
				Long idActual = siguiente.getId();
				boolean isActual = siguiente.equals(actual);				
				estadosLine.add(new EstadoProyectoLineDto(siguiente, isActual, completadoFlag));
				siguiente = requeridos.stream().filter(estado -> estado.getIdEstadoAnterior().equals(idActual) && estado.getRequerido())
						.findFirst().orElse(null);
				if(isActual)
					completadoFlag = false;
			}
		} else {
			if (actual.equals(cero)) {
				return estadosLine;
			}
			estadosLine.add(new EstadoProyectoLineDto(actual, true, false));
			EstadoProyecto anterior = findEstadoById(actual.getIdEstadoAnterior());
			while(anterior != null && !anterior.equals(cero)) {
				estadosLine.add(1, new EstadoProyectoLineDto(anterior, false, true));				
				anterior = findEstadoById(anterior.getIdEstadoAnterior());
			}
		}
		return estadosLine;
	}

}
