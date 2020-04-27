package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.actividades.Notificacion;
import com.asisge.apirest.model.entity.proyectos.Proyecto;

public interface INotificacionDao extends JpaRepository<Notificacion, Long> {
	
	List<Notificacion> findByProyecto(Proyecto proyecto, Sort sort);
}
