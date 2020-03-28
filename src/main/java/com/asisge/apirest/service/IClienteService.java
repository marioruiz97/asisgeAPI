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

	/**
	 * Dado un objeto ClienteDto lo transforma y retorna en Cliente entity
	 * 
	 * @param dto - objeto dto que será convertido a entity
	 * @return objeto Cliente (entity) para persistencia
	 */
	Cliente buildEntity(ClienteDto dto);

	/**
	 * Método para la gestión de contactos de un cliente. Este método puede guardar
	 * una nueva lista de contactos, actualizar una lista o eliminarla. Si desea
	 * gestionar un solo contacto use {@link #saveContacto(ContactoCliente)}
	 * 
	 * @param contactos   List<T> - lista de contactos dto
	 * @param persistList List<T> - lista de contactos a persistir en base de datos
	 */
	void setContactos(List<ContactoDto> contactos, List<ContactoCliente> persistList);

	List<ContactoCliente> findContactosByCliente(Long id);

	ContactoCliente findContactoById(Long id);

	/**
	 * Método para guardar un contacto de forma individual.
	 * 
	 * @see {@link #setContactos(List, List)} para guardar listas de contactos
	 * @param contacto - objeto a guardar en base de datos
	 * @return ContactoCliente - objeto guardado en base de datos
	 */
	ContactoCliente saveContacto(ContactoCliente contacto);
}
