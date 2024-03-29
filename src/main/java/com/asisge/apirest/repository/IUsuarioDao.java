package com.asisge.apirest.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asisge.apirest.model.entity.terceros.Role;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long> {

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query("UPDATE Usuario u SET u.estado = :estado WHERE u.id = :id")
	void updateEstadoUsuario(@Param("id") Long id, @Param("estado") Boolean estado);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query("UPDATE Usuario u SET u.contrasena = :contrasena WHERE u.id = :id")
	void changePasswordUsuario(@Param("id") Long id, @Param("contrasena") String contrasena);
	
	Optional<Usuario> findByCorreoIgnoreCase(String correo);
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	List<Usuario> findByRoles(Role role);
	
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	@Query("SELECT role FROM Role role WHERE role.nombreRole = ?1")
	Optional<Role> findRoleByNombre(String nombre);
}
