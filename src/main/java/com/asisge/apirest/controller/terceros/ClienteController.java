package com.asisge.apirest.controller.terceros;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.TercerosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.terceros.ClienteDto;
import com.asisge.apirest.model.dto.terceros.ContactoDto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.ContactoCliente;
import com.asisge.apirest.service.IClienteService;

@RestController
@RequestMapping("/api/")
public class ClienteController extends BaseController {

	private static final String ID_CLIENTE = "idCliente";

	@Autowired
	private IClienteService service;

	@GetMapping(TercerosPath.CLIENTES)
	public ResponseEntity<ApiResponse> findAll() {
		List<Cliente> clientes = service.findAllClientes();
		if (clientes.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(clientes), HttpStatus.OK);
	}

	@GetMapping(TercerosPath.CLIENTE_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable(ID_CLIENTE) Long id) {
		Cliente cliente = service.findClienteById(id);
		if (cliente == null) {
			return respondNotFound(id.toString());
		}
		return new ResponseEntity<>(buildOk(cliente), HttpStatus.OK);
	}

	@PostMapping(TercerosPath.CLIENTES)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ClienteDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Cliente newCliente = service.buildEntity(dto);
		if (!dto.getContactos().isEmpty()) {
			List<ContactoCliente> contactos = dto.getContactos().stream()
					.map(con -> new ContactoCliente(null, con.getNombre(), con.getTelefono(), con.getCorreo()))
					.collect(Collectors.toList());
			newCliente.setContactos(contactos);
		}
		newCliente = service.saveCliente(newCliente);
		String descripcion = String.format(RESULT_CREATED, newCliente.toString(), newCliente.getIdCliente());
		auditManager.saveAudit("correo@correo.com", ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newCliente, ""), HttpStatus.CREATED);
	}

	@PatchMapping(TercerosPath.CLIENTE_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody ClienteDto dto, BindingResult result,
			@PathVariable(ID_CLIENTE) Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		} else if (service.findClienteById(id) == null) {
			return respondNotFound(id.toString());
		}
		// se crea cliente y se setean parametros
		Cliente cliente = service.buildEntity(dto);
		cliente.setIdCliente(id);
		cliente.setContactos(service.findContactosByCliente(id));
		if (!dto.getContactos().isEmpty()) {
			service.setContactos(dto.getContactos(), cliente.getContactos());
		}
		// se ejecutan acciones en base de datos
		cliente = service.saveCliente(cliente);
		String descripcion = String.format(RESULT_UPDATED, cliente.toString(), id);
		auditManager.saveAudit("correo@correo.com", ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, cliente, ""), HttpStatus.CREATED);
	}

	@DeleteMapping(TercerosPath.CLIENTE_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_CLIENTE) Long id, @RequestParam String email) {
		try {
			service.deleteCliente(id);
			ApiResponse response = buildDeleted("Cliente", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(email, ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Cliente", id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

	@GetMapping(TercerosPath.CONTACTOS)
	public ResponseEntity<ApiResponse> findAllContactos(@PathVariable(ID_CLIENTE) Long id) {
		List<ContactoCliente> contactos = service.findContactosByCliente(id);
		if (contactos.isEmpty())
			return respondNotFound(id.toString());

		return new ResponseEntity<>(buildOk(contactos), HttpStatus.OK);
	}

	@PatchMapping(TercerosPath.CONTACTOS)
	public ResponseEntity<ApiResponse> setContactos(@Valid @RequestBody @UniqueElements List<ContactoDto> contactos,
			BindingResult result, @PathVariable(ID_CLIENTE) Long id) {
		if (result.hasErrors())
			return validateDto(result);
		Cliente cliente = service.findClienteById(id);
		if (cliente == null)
			return respondNotFound(id.toString());
		service.setContactos(contactos, cliente.getContactos());
		cliente = service.saveCliente(cliente);
		String descripcion = String.format(Messages.getString("message.result.set-contacts"), cliente.toString(), id);
		auditManager.saveAudit("correo@correo.com", ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, cliente.getContactos(), ""), HttpStatus.ACCEPTED);
	}

	@PatchMapping(TercerosPath.CONTACTO_ID)
	public ResponseEntity<ApiResponse> updateContacto(@Valid @RequestBody ContactoDto dto, BindingResult result,
			@PathVariable("id") Long id) {
		if (result.hasErrors())
			return validateDto(result);
		if (service.findContactoById(id) == null)
			return respondNotFound(id.toString());

		ContactoCliente persist = service
				.saveContacto(new ContactoCliente(id, dto.getNombre(), dto.getTelefono(), dto.getCorreo()));
		return new ResponseEntity<>(buildSuccess(RESULT_UPDATED, persist, persist.toString(), id.toString()),
				HttpStatus.ACCEPTED);
	}

}
