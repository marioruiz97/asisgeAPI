package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;

public interface IAsesorService {

	UsuarioCliente saveUsuarioCliente(UsuarioCliente usuarioCliente);

	List<UsuarioCliente> findAll();

	List<Usuario> findAsesoresById(Long idCliente);

	List<Cliente> findClientesById(Long idUsuario);

	UsuarioCliente findByClienteAndUsuario(Long idCliente, Long idUsuario);

	void deleteByUsuario(Long idUsuario);

	void deleteByCliente(Long idCliente);

}
