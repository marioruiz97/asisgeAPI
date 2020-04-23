package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.NotificacionUsuario;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface INotificacionUsuarioDao extends JpaRepository<NotificacionUsuario, Long> {
	
	List<NotificacionUsuario> findByUsuarioOrderByIdNotificacionUsuarioDesc(Usuario usuario);
}
