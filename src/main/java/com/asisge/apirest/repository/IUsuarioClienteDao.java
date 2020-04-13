package com.asisge.apirest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;

public interface IUsuarioClienteDao extends JpaRepository<UsuarioCliente, Long> {

	List<UsuarioCliente> findByUsuario(Usuario usuario);

	Optional<UsuarioCliente> findByClienteAndUsuario(Cliente cliente, Usuario usuario);

	@Query("SELECT uc.usuario FROM UsuarioCliente uc WHERE uc.cliente.idCliente =?1 ")
	List<Usuario> findAsesores(Long idCliente);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("DELETE FROM UsuarioCliente u WHERE u.usuario.idUsuario = ?1")
	void deleteByUsuario(Long idUsuario);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("DELETE FROM UsuarioCliente u WHERE u.cliente.idCliente = ?1")
	void deleteByCliente(Long idCliente);
}
