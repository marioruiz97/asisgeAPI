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
	private IEmailSenderService emailService;

	@Override
	public List<NotificacionUsuario> findByUsuario(Usuario usuario) {
		return this.notificacionUsuarioDao.findByUsuarioOrderByIdNotificacionUsuarioDesc(usuario);
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
	public void notificar(Proyecto proyecto, String mensaje, ColorNotificacion color) {
		Notificacion notificacion = new Notificacion(proyecto, color, mensaje, 8);
		repository.save(notificacion);
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
			List<String> emails = userDao.findByRoles(role).stream()
					.map(Usuario::getCorreo)
					.collect(Collectors.toList());
			emails.forEach(email -> emailService.sendNotification(email, subject, message));
		}
	}

	

}
