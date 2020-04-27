package com.asisge.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.NotificacionesPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.model.entity.actividades.NotificacionUsuario;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class NotificacionController extends BaseController {

	@Autowired
	INotificacionService service;

	@Autowired
	IUsuarioService userService;

	@GetMapping(NotificacionesPath.NOTIFICACIONES)
	public ResponseEntity<ApiResponse> getMyNotificaciones() {
		Usuario usuario = userService.findUsuarioByCorreo(getCurrentEmail());
		List<NotificacionUsuario> notificaciones = service.findByUsuario(usuario);
		return new ResponseEntity<>(buildOk(notificaciones), HttpStatus.OK);
	}

	@DeleteMapping(NotificacionesPath.NOTIFICACIONES_ID)
	public ResponseEntity<String> deleteNotificacion(@PathVariable("id") Long id) {
		service.deleteNotificacionUsuarioById(id);
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}

}
