package com.asisge.apirest.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.dto.actividades.ActividadDto;
import com.asisge.apirest.model.dto.actividades.SeguimientoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IActividadDao;
import com.asisge.apirest.repository.IEtapaPlanDao;
import com.asisge.apirest.repository.ISeguimientoDao;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.ISeguimientoService;

@Service
public class ActividadSeguimientoServiceImpl implements IActividadService, ISeguimientoService {

	@Autowired
	private IActividadDao actividadDao;

	@Autowired
	private ISeguimientoDao seguimientoDao;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IEtapaPlanDao etapaDao;

	@Override
	public List<Seguimiento> findAllSeguimientos() {
		return seguimientoDao.findAll();
	}

	@Override
	public Seguimiento findSeguimientoById(Long idSeguimiento) {
		return seguimientoDao.findById(idSeguimiento).orElse(null);
	}

	@Override
	public List<Seguimiento> findByActividad(Long idActividad) {
		Actividad actividad = findActividadById(idActividad);
		if (actividad != null) {
			return seguimientoDao.findByActividadAsociada(actividad);
		}
		return seguimientoDao.findAll();
	}

	@Override
	public Seguimiento saveSeguimiento(Seguimiento seguimiento) {
		return seguimientoDao.save(seguimiento);
	}

	@Override
	public void deleteSeguimiento(Long idSeguimiento) {
		seguimientoDao.deleteById(idSeguimiento);
	}

	@Override
	public List<Actividad> findAllActividades() {
		return actividadDao.findAll();
	}

	@Override
	public Actividad findActividadById(Long idActividad) {
		return actividadDao.findById(idActividad).orElse(null);
	}

	@Override
	public List<Actividad> findActividadesByUsuario(String email) {
		Usuario usuario = usuarioDao.findByCorreoIgnoreCase(email).orElse(null);
		if (usuario != null) {
			return actividadDao.findByResponsables(usuario);
		}
		return new ArrayList<>();
	}

	@Override
	public List<Actividad> findActividadesByEtapa(Long idEtapa) {
		EtapaPDT etapa = etapaDao.findById(idEtapa).orElse(null);
		if (etapa != null) {
			return actividadDao.findByEtapa(etapa);
		}
		return new ArrayList<>();
	}

	@Override
	public Actividad saveActividad(Actividad actividad) {
		return actividadDao.save(actividad);
	}

	@Override
	public void deleteActividad(Long idActividad) {
		actividadDao.deleteById(idActividad);
	}

	@Override
	public Seguimiento buildSeguimiento(SeguimientoDto dto) {
		return new Seguimiento(null, dto.getActividadAsociada(), new Date(), dto.getHorasTrabajadas(),
				dto.getUsuarioSeguimiento(), dto.getObservaciones(), dto.getDescripcionLabor());
	}

	@Override
	public Actividad buildActividad(ActividadDto dto) {
		return new Actividad(null, dto.getNombre(), dto.getResponsables(), dto.getEtapa(), dto.getFechaVencimiento(),
				dto.getDuracion(), dto.getDescripcion());
	}

}
