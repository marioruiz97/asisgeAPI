package com.asisge.apirest.controller.actividades;

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
import com.asisge.apirest.service.ICierreService;
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
	
	@Autowired
	private ICierreService cierreService;

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
			cierreService.validarCierreEtapa(actividad.getEtapa());
			Seguimiento seguimiento = service.buildSeguimiento(dto);
			seguimiento.setUsuarioSeguimiento(usuarioSeguimiento);
			seguimiento = service.saveSeguimiento(seguimiento);			
			String mensajeAuditoria = String.format(RESULT_CREATED,	seguimiento.toString(), seguimiento.getIdSeguimiento());
			auditManager.saveAudit(ACTION_CREATE, mensajeAuditoria);			
			return new ResponseEntity<>(buildSuccess(mensajeAuditoria, seguimiento), HttpStatus.CREATED);
		}
		String message = String.format(Messages.getString("message.error.actividad-or-user-not-found"), idActividad);
		throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
	}
	
	@PatchMapping(ProyectosPath.SEGUIMIENTO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody SeguimientoDto dto, BindingResult result, HttpServletRequest request, 
			@PathVariable(ID_ACTIVIDAD) Long idActividad, @PathVariable(ID_SEGUIMIENTO) Long idSeguimiento) {
		if (result.hasErrors())
			return validateDto(result);
		Actividad actividad = actividadService.findActividadById(idActividad);		
		Usuario usuarioSeguimiento = usuarioService.findUsuarioByCorreo(getCurrentEmail());
		Seguimiento seguimiento = service.findSeguimientoById(idSeguimiento);
		if (actividad != null && (request.isUserInRole("ROLE_ADMIN") || usuarioSeguimiento.equals(seguimiento.getUsuarioSeguimiento()))) {
			cierreService.validarCierreEtapa(actividad.getEtapa());
			seguimiento = service.buildSeguimiento(dto);
			seguimiento.setIdSeguimiento(idSeguimiento);
			seguimiento.setUsuarioSeguimiento(usuarioSeguimiento);
			seguimiento = service.saveSeguimiento(seguimiento);
			String mensajeAuditoria = String.format(RESULT_UPDATED, seguimiento.toString(), seguimiento.getIdSeguimiento());
			auditManager.saveAudit(ACTION_UPDATE, mensajeAuditoria);
			return new ResponseEntity<>(buildSuccess(mensajeAuditoria, seguimiento), HttpStatus.CREATED);
		}
		String message = Messages.getString("message.error.update-seguimiento");
		throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(ProyectosPath.SEGUIMIENTO_ID)
	public ResponseEntity<ApiResponse> delete(HttpServletRequest request, @PathVariable(ID_ACTIVIDAD) Long idActividad, @PathVariable(ID_SEGUIMIENTO) Long idSeguimiento) {
		Actividad actividad = actividadService.findActividadById(idActividad);
		Seguimiento seguimiento = service.findSeguimientoById(idSeguimiento);
		Usuario usuarioSeguimiento = usuarioService.findUsuarioByCorreo(getCurrentEmail());
		if (actividad != null && actividad.equals(seguimiento.getActividadAsociada())) {
			if (request.isUserInRole("ROLE_ADMIN") || seguimiento.getUsuarioSeguimiento().equals(usuarioSeguimiento)) {
				service.deleteSeguimiento(idSeguimiento);
				ApiResponse response = buildDeleted("Seguimiento", idSeguimiento.toString());
				String descripcion = response.getMessage();
				auditManager.saveAudit(ACTION_DELETE, descripcion);
				return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
			}
			String message = Messages.getString("message.error.delete-seguimiento");
			throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
		}
		String message = String.format(Messages.getString("message.error.actividad-or-user-not-found"), idActividad);
		throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
	}
	
}
