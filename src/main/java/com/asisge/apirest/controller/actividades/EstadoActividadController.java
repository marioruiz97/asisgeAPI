package com.asisge.apirest.controller.actividades;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.actividades.EstadoActividadDto;
import com.asisge.apirest.model.entity.actividades.EstadoActividad;
import com.asisge.apirest.service.IEstadoActividadService;

@RestController
public class EstadoActividadController extends BaseController {

	private static final String ID_ESTADO = "idEstado";

	@Autowired
	private IEstadoActividadService service;

	@GetMapping(MaestrosPath.ESTADO_ACTIVIDAD)
	public ResponseEntity<ApiResponse> findAll() {
		List<EstadoActividad> estados = service.findAll();
		if (estados.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(estados), HttpStatus.OK);
	}

	@GetMapping(MaestrosPath.ESTADO_ACTIVIDAD_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable(ID_ESTADO) Long id) {
		EstadoActividad estado = service.findEstadoById(id);
		if (estado == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(estado), HttpStatus.OK);
	}

	@Secured("ROLE_ADMIN")
	@PostMapping(MaestrosPath.ESTADO_ACTIVIDAD)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody EstadoActividadDto dto) {
		EstadoActividad newEstado = service.buildEstado(dto);
		newEstado = service.saveEstado(newEstado);
		String mensaje = String.format(RESULT_CREATED, newEstado.toString(), newEstado.getIdEstado());
		auditManager.saveAudit(newEstado.getCreatedBy(), ACTION_CREATE, mensaje);
		return new ResponseEntity<>(buildSuccess(mensaje, newEstado), HttpStatus.CREATED);
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping(MaestrosPath.ESTADO_ACTIVIDAD_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody EstadoActividadDto dto, @PathVariable(ID_ESTADO) Long id) {
		if (service.findEstadoById(id) != null) {
			EstadoActividad estado = service.buildEstado(dto);
			estado.setIdEstado(id);
			estado = service.saveEstado(estado);
			String mensaje = String.format(RESULT_UPDATED, estado.toString(), estado.getIdEstado());
			auditManager.saveAudit(estado.getCreatedBy(), ACTION_UPDATE, mensaje);
			return new ResponseEntity<>(buildSuccess(mensaje, estado), HttpStatus.CREATED);
		} else {
			return respondNotFound(id.toString());
		}
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping(MaestrosPath.ESTADO_ACTIVIDAD_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_ESTADO) Long id) {
		try {
			service.deleteEstado(id);
			ApiResponse response = buildDeleted("Estado de Actividad", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Estado de Actividad", id.toString());
			return new ResponseEntity<>(buildFail(message), HttpStatus.BAD_REQUEST);
		}
	}

}
