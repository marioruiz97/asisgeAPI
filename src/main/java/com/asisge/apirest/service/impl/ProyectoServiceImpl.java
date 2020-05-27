package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.proyectos.Dashboard;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.repository.IProyectoDao;
import com.asisge.apirest.service.IProyectoService;

@Service
public class ProyectoServiceImpl implements IProyectoService {

	@Autowired
	private IProyectoDao repository;

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
	public Proyecto findProyectoById(Long idProyecto) {
		return repository.findById(idProyecto).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Dashboard loadDashboard(Long id) {
		Proyecto proyecto = repository.findById(id).orElse(null);
		Cliente cliente = proyecto.getCliente();
		return new Dashboard(id, cliente, null, proyecto, proyecto.getEstadoProyecto(), null, null, null);
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
		return new Proyecto(null, dto.getNombreProyecto(), dto.getDescripcionGeneral(), dto.getFechaCierreProyecto(), estado, cliente, null, null);
	}

}
