package com.asisge.apirest.controller.terceros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.TercerosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.model.entity.terceros.UsuarioCliente;
import com.asisge.apirest.service.IAsesorService;


@RestController
public class AsesoresClientesController extends BaseController {

	@Autowired
	private IAsesorService service;

	/**
	 * funcionamiento var action 0 -> ningun parametro, se deben retornar todos los
	 * objetos 1 -> llega un usuario pero no llega cliente, se retornan los clientes
	 * de un usuario 2 -> llega un cliente pero no usuario, se retronan los asesores
	 * del cliente 3 -> llega los dos, se retorna el objeto usuarioCliente
	 * correspondiente (ya que usuario y clientes tienen sus claves unicas)
	 */
	@GetMapping(TercerosPath.ASESORES)
	public ResponseEntity<ApiResponse> findFiltered(@RequestParam("usuario") Optional<Long> usuario, @RequestParam("cliente") Optional<Long> cliente) {
		Long idUsuario = usuario.orElse(null);
		Long idCliente = cliente.orElse(null);
		List<?> listado;		
		
		int action;
		action = idUsuario != null ? 1 : 0;
		action = idCliente != null ? 2 : action;
		action = idUsuario != null && idCliente != null ? 3 : action;
		switch (action) {
		case 0:
			listado = service.findAll();
			break;
		case 1:
			listado = service.findClientesById(idUsuario);
			break;
		case 2:
			listado = service.findAsesoresById(idCliente);
			break;
		case 3:
			UsuarioCliente uc = service.findByClienteAndUsuario(idCliente, idUsuario);
			listado = uc != null ? Arrays.asList(uc) : new ArrayList<>();
			break;
		default:
			listado = new ArrayList<>();
			break;
		}
		if (listado.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(listado), HttpStatus.OK);
	}

	@PatchMapping(TercerosPath.ASESORES + "/{usuario}")
	public ResponseEntity<ApiResponse> setUsuariosClientes(@RequestBody List<ModelMap> dtos, @NotNull @PathVariable("usuario") Long idUsuario) {
		service.deleteByUsuario(idUsuario);
		List<UsuarioCliente> newList = dtos.stream().map(dto -> {
			Long idCliente = Long.parseLong(dto.getAttribute("cliente").toString());
			return new UsuarioCliente(null, new Usuario(idUsuario), new Cliente(idCliente));
		}).collect(Collectors.toList());
		newList.forEach(uc -> service.saveUsuarioCliente(uc));
		String message = newList.isEmpty() ? Messages.getString("message.result.delete-user-clients") : Messages.getString("message.result.multiple-client");
		auditManager.saveAudit(ACTION_UPDATE, message);
		return new ResponseEntity<>(buildSuccess(message, newList), HttpStatus.CREATED);
	}

	@PatchMapping(TercerosPath.CLIENTE_ASESOR)
	public ResponseEntity<ApiResponse> setAsesores(@RequestBody List<ModelMap> dtos, @NotNull @PathVariable("idCliente") Long idCliente) {
		service.deleteByCliente(idCliente);
		List<UsuarioCliente> newList = dtos.stream().map(dto -> {
			Long idUsuario = Long.parseLong(dto.getAttribute("usuario").toString());
			return new UsuarioCliente(null, new Usuario(idUsuario), new Cliente(idCliente));
		}).collect(Collectors.toList());
		newList.forEach(uc -> service.saveUsuarioCliente(uc));
		String message = Messages.getString("message.result.updated-client-user");
		auditManager.saveAudit(ACTION_UPDATE, message);
		return new ResponseEntity<>(buildSuccess(message, newList), HttpStatus.CREATED);
	}
		

}
