package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IActividadDao extends JpaRepository<Actividad, Long> {

	@Transactional(readOnly = true)	
	List<Actividad> findByResponsables(Usuario responsables);
	
	@Transactional(readOnly = true)	
	List<Actividad> findByEtapa(EtapaPDT etapa);
}