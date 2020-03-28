package com.asisge.apirest.controller.terceros;

import java.util.List;

import javax.validation.Valid;

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
import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IUsuarioService;

@RestController
@RequestMapping("/api/")
public class UsuarioController extends BaseController {

	private static final String CHANGE_STATUS = Messages.getString("message.action.change-status");

	@Autowired
	private IUsuarioService service;

	@GetMapping(TercerosPath.USUARIOS)
	public ResponseEntity<ApiResponse> findAll() {
		List<Usuario> usuarios = service.findAllUsuarios();
		if (usuarios.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(usuarios), HttpStatus.OK);
	}

	@GetMapping(TercerosPath.USUARIO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable("idUsuario") Long id) {
		Usuario usuario = service.findUsuarioById(id);
		if (usuario == null) {
			return respondNotFound(id.toString());
		}
		return new ResponseEntity<>(buildOk(usuario), HttpStatus.OK);
	}

	@PostMapping(TercerosPath.USUARIOS)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody UsuarioDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Usuario newUsuario = service.buildEntity(dto);
		newUsuario = service.saveUsuario(newUsuario);
		String descripcion = String.format(RESULT_CREATED, newUsuario.toString(), newUsuario.getId());
		auditManager.saveAudit("correo@correo.com", ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newUsuario, ""), HttpStatus.CREATED);
	}

	@PatchMapping(TercerosPath.USUARIO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody UsuarioDto dto, BindingResult result, @PathVariable("idUsuario") Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		} else if (service.findUsuarioById(id) == null) {
			return respondNotFound(id.toString());
		}
		Usuario usuario = service.buildEntity(dto);
		usuario.setId(id);
		usuario = service.saveUsuario(usuario);
		String descripcion = String.format(RESULT_UPDATED, usuario.toString(), id);
		auditManager.saveAudit("correo@correo.com", ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, usuario, ""), HttpStatus.CREATED);
	}

	@DeleteMapping(TercerosPath.USUARIO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable("idUsuario") Long id, @RequestParam String email) {
		try {
			service.deleteUsuario(id);
			ApiResponse response = buildDeleted("Usuario", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(email, ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Usuario", id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

	@GetMapping(TercerosPath.CAMBIO_ESTADO)
	public ResponseEntity<ApiResponse> cambioEstadoUsuario(@PathVariable("idUsuario") Long id, @RequestParam @Valid Boolean estado) {
		Usuario usuario = service.findUsuarioById(id);
		if (usuario == null)
			return respondNotFound(id.toString());

		String mensaje;
		if (usuario.getEstado().equals(estado)) {
			// mensaje de que el estado no se ha cambiado por que es el mismo
			mensaje = Messages.getString("massage.resut.changestate.nochange");
		} else {
			// se setea nuevo estado, se crea mensaje correspondiente para la auditoria
			usuario.setEstado(estado);
			usuario = service.changeEstadoUsuario(id, estado);
			if (estado.booleanValue()) {
				// mensaje si se activa
				mensaje = Messages.getString("massage.resut.changestate.activate");
			} else {
				// mensaje si se desactiva
				mensaje = Messages.getString("massage.resut.changestate.deactivate");
			}
			auditManager.saveAudit("prueba@correo.com", CHANGE_STATUS, String.format(mensaje, usuario.toString()));
		}
		return new ResponseEntity<>(buildSuccess(mensaje, usuario, usuario.toString()), HttpStatus.ACCEPTED);
	}
}
