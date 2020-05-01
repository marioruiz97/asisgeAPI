package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.actividades.Notificacion;
import com.asisge.apirest.model.entity.actividades.NotificacionUsuario;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface INotificacionService {

	List<NotificacionUsuario> findByUsuario(Usuario usuario);

	void deleteNotificacionUsuarioById(Long id);

	List<Notificacion> findByProyecto(Proyecto proyecto);
	
	void notificarAdmins(String nombreProyecto);
	
	void notificarAdmins(Proyecto proyecto, String mensaje, ColorNotificacion color);

	void notificarProyecto(Proyecto proyecto, String mensaje, ColorNotificacion color);
	
	void notificarUsuariosProyectos(Proyecto proyecto, String mensaje, ColorNotificacion color);

	void notificarUsuario(Proyecto proyecto, Usuario usuario, String mensaje, ColorNotificacion color, int duracion);
	
}
