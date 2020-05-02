package com.asisge.apirest.controller.terceros;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.TercerosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.terceros.UsuarioDto;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IEmailSenderService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class UsuarioController extends BaseController {

	private static final String CHANGE_STATUS = Messages.getString("message.action.change-status");

	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private IEmailSenderService emailService;
	
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

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@PostMapping(TercerosPath.USUARIOS)
	public ResponseEntity<ApiResponse> create
		(HttpServletRequest request, @Valid @RequestBody UsuarioDto dto, BindingResult result) 
	{
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Usuario newUsuario = service.buildEntity(dto);
		newUsuario = service.saveUsuario(newUsuario);
		VerificationToken token = service.createVerificationToken(newUsuario);
		emailService.sendConfirmationEmail(token);
		String descripcion = String.format(RESULT_CREATED, newUsuario.toString(), newUsuario.getIdUsuario());
		auditManager.saveAudit(newUsuario.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newUsuario), HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping(TercerosPath.USUARIO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody UsuarioDto dto, BindingResult result, @PathVariable("idUsuario") Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Usuario old = service.findUsuarioById(id);
		if (old == null) {
			return respondNotFound(id.toString());
		}
		Usuario usuario = service.buildEntity(dto);
		usuario.setIdUsuario(id);
		usuario.setVerificado(old.getVerificado());
		usuario = service.saveUsuario(usuario);
		String descripcion = String.format(RESULT_UPDATED, usuario.toString(), id);
		auditManager.saveAudit(usuario.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, usuario), HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping(TercerosPath.USUARIO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable("idUsuario") Long id) {		
			service.deleteUsuario(id);
			ApiResponse response = buildDeleted("Usuario", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);		
	}

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@GetMapping(TercerosPath.CAMBIO_ESTADO)
	public ResponseEntity<ApiResponse> cambioEstadoUsuario(@PathVariable("idUsuario") Long id, @RequestParam @Valid Boolean estado) {
		Usuario usuario = service.findUsuarioById(id);
		if (usuario == null)
			return respondNotFound(id.toString());

		String mensaje;
		if (usuario.getEstado().equals(estado)) {
			// mensaje de que el estado no se ha cambiado por que es el mismo
			mensaje = Messages.getString("massage.result.changestate.nochange");
		} else {
			// se setea nuevo estado, se crea mensaje correspondiente para la auditoria
			usuario.setEstado(estado);
			usuario = service.changeEstadoUsuario(id, estado);
			if (estado.booleanValue()) {
				// mensaje si se activa
				mensaje = Messages.getString("massage.result.changestate.activate");
			} else {
				// mensaje si se desactiva
				mensaje = Messages.getString("massage.result.changestate.deactivate");
			}
			auditManager.saveAudit(CHANGE_STATUS, String.format(mensaje, usuario.toString()));
		}
		return new ResponseEntity<>(buildSuccess(mensaje, usuario, usuario.toString()), HttpStatus.ACCEPTED);
	}
}
