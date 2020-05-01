package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.dto.terceros.ClienteDto;
import com.asisge.apirest.model.dto.terceros.ContactoDto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.ContactoCliente;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.repository.IClienteDao;
import com.asisge.apirest.service.IAsesorService;
import com.asisge.apirest.service.IClienteService;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao repository;

	@Autowired
	private IAsesorService asesorService;

	@Override
	public Cliente saveCliente(Cliente cliente) {
		return repository.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAllClientes() {
		return repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findClienteById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public void deleteCliente(Long id) {
		asesorService.deleteByCliente(id);
		repository.deleteById(id);
	}

	@Override
	public Cliente buildEntity(ClienteDto dto) {
		TipoDocumento tipoDoc = new TipoDocumento();
		tipoDoc.setId(dto.getTipoDocumento());
		return new Cliente(null, dto.getIdentificacion(), dto.getNombreComercial(), dto.getRazonSocial(), tipoDoc, null);
	}

	@Override
	public void setContactos(List<ContactoDto> contactos, List<ContactoCliente> persistLists) {
		List<ContactoCliente> newList = contactos.stream()
				.map(dto -> new ContactoCliente(null, dto.getNombre(), dto.getTelefono(), dto.getCorreo()))
				.collect(Collectors.toList());

		// Si la lista vieja esta vacia se settea directamente la nueva
		if (!persistLists.isEmpty()) {
			// Si el nuevo contacto ya existe en la lista vieja, no se modifica y los contactos viejos que no estan en la nueva se eliminan
			persistLists.retainAll(newList);
			newList.removeAll(persistLists);
		}
		// Se agregan elementos no existentes en la lista vieja
		persistLists.addAll(newList);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContactoCliente> findContactosByCliente(Long id) {
		return repository.findContactosById(id, Sort.by(Direction.ASC, "id"));
	}

}
