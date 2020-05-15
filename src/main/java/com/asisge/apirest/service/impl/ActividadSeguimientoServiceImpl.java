package com.asisge.apirest.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.actividades.ActividadDto;
import com.asisge.apirest.model.dto.actividades.SeguimientoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.actividades.EstadoActividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.terceros.Role;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.IActividadDao;
import com.asisge.apirest.repository.IEtapaPlanDao;
import com.asisge.apirest.repository.ISeguimientoDao;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.IEmailSenderService;
import com.asisge.apirest.service.IEstadoActividadService;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.ISeguimientoService;
import com.asisge.apirest.service.IUsuarioService;

@Service
public class ActividadSeguimientoServiceImpl implements IActividadService, ISeguimientoService {

	@Autowired
	private IActividadDao actividadDao;

	@Autowired
	private ISeguimientoDao seguimientoDao;

	@Autowired
	private IUsuarioDao usuarioDao;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IEtapaPlanDao etapaDao;

	@Autowired
	private IEstadoActividadService estadoActividadService;
	
	@Autowired
	private INotificacionService notificacionService;
	
	@Autowired
	private IEmailSenderService emailService;

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
			return seguimientoDao.findByActividadAsociadaOrderByIdSeguimientoAsc(actividad);
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
	public List<Actividad> findActividadesByPlan(PlanDeTrabajo plan) {
		List<Actividad> actividades = new ArrayList<>();
		if (!plan.getEtapas().isEmpty())
			plan.getEtapas().forEach(etapa -> actividades.addAll(findActividadesByEtapa(etapa.getIdEtapaPDT())));
		return actividades;
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
	public Actividad changeEstadoActividad(HttpServletRequest request, Actividad actividad, Long idNuevoEstado) {		
		EstadoActividad newEstado = estadoActividadService.findEstadoById(idNuevoEstado);
		
		if(newEstado.getActividadCompletada().booleanValue() && !request.isUserInRole("ROLE_ADMIN"))
			throw new InvalidProcessException(HttpStatus.FORBIDDEN);
		
		if(!actividad.getEstadoActividad().equals(newEstado)) {
			actividad.setEstadoActividad(newEstado);
			actividad = saveActividad(actividad);
		}
		if(newEstado.getActividadNoAprobada().booleanValue()) {
			notificarNoAprobada(actividad);
		}
		if(newEstado.getActividadCompletada().booleanValue()) {
			notificarCompletada(actividad);
		}
		return actividad;
	}
	
	
	@Override
	public void notificarCompletada(Actividad actividad) {
		String mensaje = String.format(Messages.getString("notification.activity.completed"), actividad.getIdActividad(), actividad.getNombre());
		List<Long> usuarios = actividad.getResponsables().stream().map(Usuario::getIdUsuario).collect(Collectors.toList());
		notificacionService.notificarResponsablesActividad(usuarios, mensaje);
	}

	@Override
	public void notificarNoAprobada(Actividad actividad) {
		String mensaje = String.format(Messages.getString("notification.activity.not-passed"), actividad.getIdActividad(), actividad.getNombre());
		List<Long> usuarios = actividad.getResponsables().stream().map(Usuario::getIdUsuario).collect(Collectors.toList());
		List<String> emails = actividad.getResponsables().stream().map(Usuario::getCorreo).collect(Collectors.toList());
		notificacionService.notificarResponsablesActividad(usuarios, mensaje);
		String subject = Messages.getString("notification.activity.subject");
		emailService.sendNotifications(emails.toArray(new String[0]), subject, mensaje);
	}

	@Override
	public void solicitarAprobacion(Actividad actividad) {
		String mensaje = String.format(Messages.getString("notification.activity.review-activity"), actividad.getIdActividad(), actividad.getNombre());
		notificacionService.notificarAdmins(null, mensaje, ColorNotificacion.INFO);
		String subject = Messages.getString("notification.activity.subject");
		Role role = usuarioDao.findRoleByNombre("ROLE_ADMIN").orElse(null);
		List<String> emails = actividad.getResponsables().stream().map(Usuario::getCorreo).collect(Collectors.toList());
		emails.addAll(usuarioDao.findByRoles(role).stream().map(Usuario::getCorreo).collect(Collectors.toList()));
		emails = emails.stream().distinct().collect(Collectors.toList());
		if(!emails.isEmpty())			
			emailService.sendNotifications(emails.toArray(new String[0]), subject, mensaje);
	}

	@Override
	public void deleteActividad(Long idActividad) {
		List<Seguimiento> seguimientos = findByActividad(idActividad);
		if (!seguimientos.isEmpty()) {
			seguimientoDao.deleteAll(seguimientos);
		}
		actividadDao.deleteById(idActividad);
	}

	@Override
	public void setResponsables(Actividad actividad, List<Long> usuarios) {
		if (usuarios != null && !usuarios.isEmpty()) {
			List<Usuario> responsables = usuarios.stream()
					.map(idUsuario -> usuarioService.findUsuarioById(idUsuario))
					.collect(Collectors.toList());
			actividad.setResponsables(responsables);
		} else {
			actividad.setResponsables(new ArrayList<>());
		}
	}

	@Override
	public Seguimiento buildSeguimiento(SeguimientoDto dto) {
		Actividad actividad = findActividadById(dto.getActividadAsociada());
		return new Seguimiento(null, actividad, dto.getHorasTrabajadas(), null, dto.getObservaciones(), dto.getDescripcionLabor());
	}

	@Override
	public Actividad buildActividad(ActividadDto dto) {
		EtapaPDT etapa = etapaDao.findById(dto.getEtapa()).orElse(null);
		EstadoActividad estado = estadoActividadService.findEstadoById(dto.getEstadoActividad());
		return new Actividad(null, dto.getNombre(), null, etapa, estado, dto.getFechaVencimiento(), dto.getDuracion(), dto.getDescripcion());
	}
	

}
