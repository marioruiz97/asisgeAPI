package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.repository.IEstadoProyectoDao;
import com.asisge.apirest.repository.IProyectoDao;
import com.asisge.apirest.service.IEstadoProyectoService;
import com.asisge.apirest.service.IProyectoService;

@Service
public class ProyectoServiceImpl implements IProyectoService, IEstadoProyectoService {

	@Autowired
	private IEstadoProyectoDao estadoProyectoDao;

	@Autowired
	private IProyectoDao repository;

	private boolean validEstadoAnterior(Long idAnterior) {
		boolean result = false;
		if (estadoProyectoDao.findByIdEstadoAnterior(null).isEmpty() && idAnterior == null)
			result = true;
		if (idAnterior != null && estadoProyectoDao.existsById(idAnterior))
			result = true;
		return result;
	}

	@Override
	public EstadoProyecto saveEstado(EstadoProyecto estado) {
		if (!validEstadoAnterior(estado.getIdEstadoAnterior())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					Messages.getString("message.error.create.estado-proyecto"));
		}
		return estadoProyectoDao.save(estado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstadoProyecto> findAllEstados() {
		return estadoProyectoDao.findAll(Sort.by(Direction.ASC, "id"));
	}

	@Override
	@Transactional(readOnly = true)
	public EstadoProyecto findEstadoById(Long id) {
		return estadoProyectoDao.findById(id).orElse(null);
	}

	@Override
	public void deleteEstado(Long id) {
		estadoProyectoDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EstadoProyecto> findNextEstados(Long estadoActual) {
		return estadoProyectoDao.findByIdEstadoAnterior(estadoActual);
	}

	@Override
	public Proyecto saveProyecto(Proyecto proyecto) {
		return repository.save(proyecto);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Proyecto> findAllProyectos() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Proyecto findProyecto(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public void deleteProyecto(Long id) {
		repository.deleteById(id);
	}

	@Override
	public Proyecto buildEntity(ProyectoDto dto) {
		Cliente cliente = new Cliente(dto.getCliente());
		EstadoProyecto estado = new EstadoProyecto();
		estado.setId(dto.getEstadoProyecto());
		return new Proyecto(null, dto.getNombreProyecto(), dto.getDescripcionGeneral(), dto.getFechaCierreProyecto(),
				estado, null, cliente);
	}

}
