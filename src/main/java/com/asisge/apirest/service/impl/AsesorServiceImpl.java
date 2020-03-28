package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;
import com.asisge.apirest.repository.IClienteDao;
import com.asisge.apirest.repository.IUsuarioClienteDao;
import com.asisge.apirest.repository.IUsuarioDao;
import com.asisge.apirest.service.IAsesorService;

@Service
public class AsesorServiceImpl implements IAsesorService {

	@Autowired
	private IUsuarioClienteDao repository;

	@Autowired
	private IUsuarioDao userDao;

	@Autowired
	private IClienteDao clienteDao;

	@Override
	public List<UsuarioCliente> findAll() {
		return repository.findAll();
	}

	@Override
	public List<Usuario> findAsesoresById(Long idCliente) {
		return repository.findAsesores(idCliente);
	}

	@Override
	public List<Cliente> findClientesById(Long idUsuario) {
		Usuario usuario = userDao.findById(idUsuario).orElse(null);
		return repository.findByUsuario(usuario).stream().map(UsuarioCliente::getCliente).collect(Collectors.toList());
	}

	@Override
	public UsuarioCliente findByClienteAndUsuario(Long idCliente, Long idUsuario) {
		Cliente cliente = clienteDao.findById(idCliente).orElse(null);
		Usuario usuario = userDao.findById(idUsuario).orElse(null);
		return repository.findByClienteAndUsuario(cliente, usuario).orElse(null);
	}

	@Override
	public UsuarioCliente saveUsuarioCliente(UsuarioCliente usuarioCliente) {
		return repository.save(usuarioCliente);
	}

	@Override
	public void deleteUsuarioCliente(Long id) {
		repository.deleteById(id);		
	}

}
