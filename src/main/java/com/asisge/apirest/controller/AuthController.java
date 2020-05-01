package com.asisge.apirest.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.AuthPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.terceros.CuentaDto;
import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IEmailSenderService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
@RequestMapping("/auth/")
public class AuthController extends BaseController {

	@Autowired
	private IUsuarioService service;

	@Autowired
	private IEmailSenderService emailService;

	@GetMapping(AuthPath.ME)
	public ResponseEntity<ApiResponse> findMyInfo() {
		Usuario usuario = service.findUsuarioByCorreo(getCurrentEmail());
		if (usuario == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(buildOk(usuario), HttpStatus.OK);
	}

	@PostMapping(AuthPath.ME)
	public ResponseEntity<ApiResponse> saveMyInfo(@Valid @RequestBody CuentaDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Usuario user = service.findUsuarioById(dto.getIdUsuario());
		user.setApellido1(dto.getApellido1());
		user.setApellido2(dto.getApellido2());
		user.setCorreo(dto.getCorreo());
		user.setNombre(dto.getNombre());
		user.setTelefono(dto.getTelefono());
		user = service.saveUsuario(user);
		String descripcion = String.format(RESULT_UPDATED, user.toString(), user.getIdUsuario());
		auditManager.saveAudit(user.getLastModifiedBy(), ACTION_UPDATE, descripcion + ". Desde mi perfil");
		return new ResponseEntity<>(buildMessage(descripcion), HttpStatus.CREATED);
	}

	@PostMapping(AuthPath.CAMBIO_CONTRASENA)
	public ResponseEntity<ApiResponse> changePassword(@RequestBody ModelMap model, @PathVariable Long id) {
		try {
			Usuario user = service.findUsuarioByCorreo(getCurrentEmail());
			user.setContrasena(model.getAttribute("password").toString());
			service.changePassword(user);
			String accion = Messages.getString("message.action.change-password");
			String descripcion = Messages.getString("message.result.password-changed");
			auditManager.saveAudit(accion, String.format(descripcion, user.getCorreo()));
			return new ResponseEntity<>(buildMessage(descripcion), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.change-password"), id.toString());
			return new ResponseEntity<>(buildFail(message), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping(AuthPath.RECUPERAR)
	public ResponseEntity<ApiResponse> recoverPassword(@RequestBody Map<String, Object> model) {
		try {
			String email = model.get("correo").toString();
			Usuario usuario = service.findUsuarioByCorreo(email);
			if (usuario == null) {
				// excepcion usada para llevar el código al bloque del catch
				throw new NullPointerException();
			}
			VerificationToken token = service.validVerificationToken(usuario);
			emailService.sendRecoveryPassword(token);
			String message = Messages.getString("message.result.recovery-sent");
			return new ResponseEntity<>(buildMessage(message), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = Messages.getString("message.error.recovery");
			return new ResponseEntity<>(buildFail(message), HttpStatus.BAD_REQUEST);
		}
	}

}
