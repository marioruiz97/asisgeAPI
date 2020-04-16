package com.asisge.apirest.controller.proyectos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.service.IEstadoProyectoService;

@RestController
public class EstadoProyectoController extends BaseController {

	@Autowired
	private IEstadoProyectoService service;

	@GetMapping(MaestrosPath.ESTADO_PROYECTO)
	public ResponseEntity<ApiResponse> findAll() {
		List<EstadoProyecto> estados = service.findAllEstados();
		if (estados.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(estados), HttpStatus.OK);
	}

	@GetMapping(MaestrosPath.ESTADO_PROYECTO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable("idEstado") Long id) {
		EstadoProyecto estado = service.findEstadoById(id);
		if (estado == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(estado), HttpStatus.OK);
	}

	@GetMapping(MaestrosPath.POSIBLES_ESTADOS)
	public ResponseEntity<ApiResponse> findPosiblesEstados(@PathVariable("idEstado") Long actual) {
		actual = actual.equals(0L) ? null : actual;
		List<EstadoProyecto> estados = service.findNextEstados(actual);
		if(estados.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(estados), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping(MaestrosPath.ESTADO_PROYECTO)
	public ResponseEntity<ApiResponse> create(@RequestBody ModelMap model) {
		EstadoProyecto newEstado = buildEstado(model);
		newEstado = service.saveEstado(newEstado);
		String descripcion = String.format(RESULT_CREATED, newEstado.toString(), newEstado.getId());
		auditManager.saveAudit(newEstado.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newEstado, ""), HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping(MaestrosPath.ESTADO_PROYECTO_ID)
	public ResponseEntity<ApiResponse> update(@RequestBody ModelMap model, @PathVariable("idEstado") Long id) {
		EstadoProyecto estado = buildEstado(model);
		estado.setId(id);
		estado = service.saveEstado(estado);
		String descripcion = String.format(RESULT_UPDATED, estado.toString(), estado.getId());
		auditManager.saveAudit(estado.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, estado, ""), HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping(MaestrosPath.ESTADO_PROYECTO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable("idEstado") Long id) {
		try {
			service.deleteEstado(id);
			ApiResponse response = buildDeleted("Estado de Proyecto", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), 
					"Estado de Proyecto",
					id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

	private static EstadoProyecto buildEstado(ModelMap model) {
		final String estadoAnterior = "idEstadoAnterior";
		String nombreEstado = model.getAttribute("nombreEstado").toString();
		String descripcion = model.getAttribute("descripcion").toString();
		Long idEstadoAnterior;
		try {
			idEstadoAnterior = convertToLong(model.getAttribute(estadoAnterior).toString(), estadoAnterior, Boolean.FALSE);
		} catch (NullPointerException e) {
			idEstadoAnterior = null;
		}
		return new EstadoProyecto(null, nombreEstado, descripcion, idEstadoAnterior);
	}

}
