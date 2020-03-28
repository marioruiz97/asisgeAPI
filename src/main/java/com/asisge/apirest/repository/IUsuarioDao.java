package com.asisge.apirest.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query("UPDATE Usuario u SET u.estado = :estado WHERE u.id = :id")
	void updateEstadoUsuario(@Param("id") Long id, @Param("estado") Boolean estado);
}
