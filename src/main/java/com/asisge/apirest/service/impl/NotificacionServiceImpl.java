package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.actividades.Notificacion;
import com.asisge.apirest.model.entity.actividades.NotificacionUsuario;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.Role;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.INotificacionDao;
import com.asisge.apirest.repository.INotificacionUsuarioDao;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IEmailSenderService;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.INotificacionService;

@Service
public class NotificacionServiceImpl implements INotificacionService {

	@Autowired
	private INotificacionDao repository;

	@Autowired
	private INotificacionUsuarioDao notificacionUsuarioDao;

	@Autowired
	private IUsuarioDao userDao;

	@Autowired
	private IMiembrosService miembrosService;

	@Autowired
	private IEmailSenderService emailService;

	@Override
	public List<NotificacionUsuario> findByUsuario(Usuario usuario) {
		return notificacionUsuarioDao.findByUsuarioOrderByIdNotificacionUsuarioDesc(usuario);
	}

	@Override
	public void deleteNotificacionUsuarioById(Long id) {
		notificacionUsuarioDao.deleteById(id);
	}

	@Override
	public List<Notificacion> findByProyecto(Proyecto proyecto) {
		return repository.findByProyecto(proyecto, Sort.by("fechaCreacion").descending());
	}

	@Override
	public void notificarProyecto(Proyecto proyecto, String mensaje, ColorNotificacion color) {
		Notificacion notificacion = new Notificacion(proyecto, color, mensaje, 8);
		repository.save(notificacion);
	}

	@Override
	public void notificarUsuariosProyectos(Proyecto proyecto, String mensaje, ColorNotificacion color) {
		Notificacion notificacion = repository.saveAndFlush(new Notificacion(proyecto, color, mensaje, 8));
		List<NotificacionUsuario> notificaciones = miembrosService.findUsuariosByProyecto(proyecto.getIdProyecto())
				.stream().map(user -> new NotificacionUsuario(null, user, notificacion, false))
				.collect(Collectors.toList());
		notificacionUsuarioDao.saveAll(notificaciones);
	}

	@Override
	public void notificarUsuario(Proyecto proyecto, Usuario usuario, String mensaje, ColorNotificacion color, int duracion) {
		Notificacion notificacion = new Notificacion(proyecto, color, mensaje, duracion);
		notificacion = repository.save(notificacion);
		NotificacionUsuario notificacionUsuario = new NotificacionUsuario(null, usuario, notificacion, false);
		notificacionUsuarioDao.save(notificacionUsuario);
	}

	@Override
	public void notificarAdmins(String nombreProyecto) {
		Role role = userDao.findRoleByNombre("ROLE_ADMIN").orElse(null);
		if (role != null) {
			final String subject = Messages.getString("notification.created-project.subject");
			final String message = String.format(Messages.getString("notification.created-project.message"), nombreProyecto);
			List<String> emails = userDao.findByRoles(role).stream().map(Usuario::getCorreo).collect(Collectors.toList());
			// emails.forEach(email -> emailService.sendNotification(email, subject, message))
			emailService.sendNotifications(emails.toArray(new String[0]), subject, message);
		}
	}

	@Override
	public void notificarAdmins(Proyecto proyecto, String mensaje, ColorNotificacion color) {
		Role role = userDao.findRoleByNombre("ROLE_ADMIN").orElse(null);
		if (role != null) {
			Notificacion notificacion = repository.save(new Notificacion(proyecto, color, mensaje, 8));
			List<NotificacionUsuario> notificaciones = userDao.findByRoles(role)
					.stream().map(user-> new NotificacionUsuario(null, user, notificacion, false))
					.collect(Collectors.toList());
			notificacionUsuarioDao.saveAll(notificaciones);
		}
	}

	@Override
	public void notificarResponsablesActividad(List<Long> usuarios, String mensaje) {
		Notificacion notificacion = repository.save(new Notificacion(null, ColorNotificacion.INFO, mensaje, 8));
		if (!usuarios.isEmpty()) {
			List<Usuario> responsables = usuarios.stream()
					.map(idUsuario -> userDao.findById(idUsuario).orElse(null))
					.collect(Collectors.toList());
			List<NotificacionUsuario> notificaciones = responsables.stream()
					.map(user -> new NotificacionUsuario(null, user, notificacion, false))
					.collect(Collectors.toList());
			notificacionUsuarioDao.saveAll(notificaciones);
		}
	}

}
