package com.asisge.apirest.controller.terceros;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.TercerosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.terceros.ClienteDto;
import com.asisge.apirest.model.dto.terceros.ContactoDto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.ContactoCliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;
import com.asisge.apirest.service.IAsesorService;
import com.asisge.apirest.service.IClienteService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class ClienteController extends BaseController {

	private static final String ID_CLIENTE = "idCliente";

	@Autowired
	private IClienteService service;

	@Autowired
	private IAsesorService asesorService;

	@Autowired
	private IUsuarioService userService;

	@GetMapping(TercerosPath.CLIENTES)
	public ResponseEntity<ApiResponse> findAll(HttpServletRequest request) {
		List<Cliente> clientes;
		if (request.isUserInRole("ROLE_ADMIN")) {
			clientes = service.findAllClientes();
		} else {
			Usuario usuario = userService.findUsuarioByCorreo(getCurrentEmail());
			clientes = asesorService.findClientesById(usuario != null ? usuario.getIdUsuario() : 0);
		}
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

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(TercerosPath.CLIENTES)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ClienteDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Cliente newCliente = service.buildEntity(dto);
		if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
			List<ContactoCliente> contactos = dto.getContactos().stream()
					.map(con -> new ContactoCliente(null, con.getNombre(), con.getTelefono(), con.getCorreo()))
					.collect(Collectors.toList());
			newCliente.setContactos(contactos);
		}
		newCliente = service.saveCliente(newCliente);
		addAsesor(newCliente);
		String descripcion = String.format(RESULT_CREATED, newCliente.toString(), newCliente.getIdCliente());
		auditManager.saveAudit(newCliente.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newCliente), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PatchMapping(TercerosPath.CLIENTE_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody ClienteDto dto, BindingResult result, @PathVariable(ID_CLIENTE) Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		} else if (service.findClienteById(id) == null) {
			return respondNotFound(id.toString());
		}
		// se crea cliente y se setean parametros
		Cliente cliente = service.buildEntity(dto);
		cliente.setIdCliente(id);
		cliente.setContactos(service.findContactosByCliente(id));
		if (dto.getContactos() != null && !dto.getContactos().isEmpty()) {
			service.setContactos(dto.getContactos(), cliente.getContactos());
		} else {
			service.setContactos(new ArrayList<ContactoDto>(), cliente.getContactos());
		}
		// se ejecutan acciones en base de datos
		cliente = service.saveCliente(cliente);
		String descripcion = String.format(RESULT_UPDATED, cliente.toString(), id);
		auditManager.saveAudit(cliente.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, cliente), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@DeleteMapping(TercerosPath.CLIENTE_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_CLIENTE) Long id) {
		service.deleteCliente(id);
		ApiResponse response = buildDeleted("Cliente", id.toString());
		String descripcion = response.getMessage();
		auditManager.saveAudit(ACTION_DELETE, descripcion);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

	private void addAsesor(Cliente cliente) {
		Usuario usuario = userService.findUsuarioByCorreo(getCurrentEmail());
		UsuarioCliente asesor = new UsuarioCliente();
		asesor.setCliente(cliente);
		asesor.setUsuario(usuario);
		asesorService.saveUsuarioCliente(asesor);
	}

}
