package com.asisge.apirest.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IUsuarioService;

@RestController
@RequestMapping("/auth/")
public class AuthController extends BaseController {

	@Autowired
	private IUsuarioService service;

	@GetMapping(AuthPath.ME_EMAIL)
	public ResponseEntity<ApiResponse> findById(@PathVariable("email") String email) {
		Usuario usuario = service.findUsuarioByCorreo(email);
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
		return new ResponseEntity<>(buildSuccess(descripcion, user, ""), HttpStatus.CREATED);
	}

	@PostMapping(AuthPath.CAMBIO_CONTRASENA)
	public ResponseEntity<ApiResponse> changePassword(@RequestBody ModelMap model, @NotNull @PathVariable("usuario") Long id) {
		try {
			Usuario user = service.findUsuarioById(id);
			user.setContrasena(model.getAttribute("password").toString());
			service.changeContrasena(user);
			String descripcion = Messages.getString("message.result.password-changed");
			auditManager.saveAudit("Cambio de contraseña", String.format(descripcion, user.getCorreo()));
			return new ResponseEntity<>(buildSuccess(descripcion, user, ""), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.change-password"), id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}

	}

}
