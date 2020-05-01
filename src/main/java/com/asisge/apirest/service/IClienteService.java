package com.asisge.apirest.service;

import com.asisge.apirest.model.dto.terceros.ClienteDto;
import com.asisge.apirest.model.dto.terceros.ContactoDto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.ContactoCliente;

import java.util.List;

public interface IClienteService {

	Cliente saveCliente(Cliente cliente);

	List<Cliente> findAllClientes();

	Cliente findClienteById(Long id);

	void deleteCliente(Long id);

	Cliente buildEntity(ClienteDto dto);

	void setContactos(List<ContactoDto> contactos, List<ContactoCliente> persistList);

	List<ContactoCliente> findContactosByCliente(Long id);

}
