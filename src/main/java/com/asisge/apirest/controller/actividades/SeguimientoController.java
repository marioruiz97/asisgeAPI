package com.asisge.apirest.controller.actividades;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.actividades.SeguimientoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.ISeguimientoService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class SeguimientoController extends BaseController {

	private static final String ID_ACTIVIDAD = "idActividad";
	private static final String ID_SEGUIMIENTO = "idSeguimiento";

	@Autowired
	private ISeguimientoService service;

	@Autowired
	private IActividadService actividadService;

	@Autowired
	private IUsuarioService usuarioService;
	

	@GetMapping(ProyectosPath.SEGUIMIENTOS)
	public ResponseEntity<ApiResponse> getAllByActividad(@PathVariable(ID_ACTIVIDAD) Long idActividad) {
		List<Seguimiento> seguimientos = service.findByActividad(idActividad);
		if (seguimientos.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(seguimientos), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.SEGUIMIENTO_ID)
	public ResponseEntity<ApiResponse> getSeguimientoById(@PathVariable(ID_SEGUIMIENTO) Long idSeguimiento) {
		Seguimiento seguimiento = service.findSeguimientoById(idSeguimiento);
		if (seguimiento == null)
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(seguimiento), HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.SEGUIMIENTOS)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody SeguimientoDto dto, BindingResult result, @PathVariable(ID_ACTIVIDAD) Long idActividad ) {
		if(result.hasErrors())
			return validateDto(result);
		Actividad actividad = actividadService.findActividadById(idActividad);
		Usuario usuarioSeguimiento = usuarioService.findUsuarioByCorreo(getCurrentEmail());
		if(actividad != null && usuarioSeguimiento != null) {
			Seguimiento seguimiento = service.buildSeguimiento(dto);
			seguimiento.setUsuarioSeguimiento(usuarioSeguimiento);
			service.saveSeguimiento(seguimiento);			
			String mensajeAuditoria = String.format(RESULT_CREATED,	seguimiento.toString(), seguimiento.getIdSeguimiento());
			auditManager.saveAudit(ACTION_CREATE, mensajeAuditoria);			
			return new ResponseEntity<>(buildSuccess(mensajeAuditoria, seguimiento), HttpStatus.CREATED);
		}
		String message = String.format(Messages.getString("message.error.actividad-or-user-not-found"), idActividad);
		throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
	}
}
