package com.asisge.apirest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;

public interface IUsuarioClienteDao extends JpaRepository<UsuarioCliente, Long> {

	/**
	 * @deprecated se depreca por que no se usa. usar en cambio {@link #findAsesores}
	 * @see {@link #findAsesores(Long)} usar ya que retorna la lista ya filtrada por clientes 
	 * @param cliente
	 * @return lista de usuarios clientes filtrado por cliente
	 */	
	@Deprecated
	List<UsuarioCliente> findByCliente(Cliente cliente);

	List<UsuarioCliente> findByUsuario(Usuario usuario);

	Optional<UsuarioCliente> findByClienteAndUsuario(Cliente cliente, Usuario usuario);

	@Query("SELECT uc.usuario FROM UsuarioCliente uc WHERE uc.cliente.idCliente =?1 ")
	List<Usuario> findAsesores(Long idCliente);
}
