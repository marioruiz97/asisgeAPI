package com.asisge.apirest.controller.proyectos;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.service.IProyectoService;

@RestController
public class ProyectoController extends BaseController {

	private static final String ID_PROYECTO = "idProyecto";

	@Autowired
	private IProyectoService service;

	@GetMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> findAll() {
		List<Proyecto> proyectos = service.findAllProyectos();
		if (proyectos.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(proyectos), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable(ID_PROYECTO) Long id) {
		Proyecto proyecto = service.findProyecto(id);
		if (proyecto == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(proyecto), HttpStatus.OK);
	}

	@PostMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProyectoDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Proyecto newProject = service.buildEntity(dto);
		newProject = service.saveProyecto(newProject);
		String descripcion = String.format(RESULT_CREATED, newProject.toString(), newProject.getIdProyecto());
		auditManager.saveAudit("correo@correo.com", ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newProject, ""), HttpStatus.CREATED);
	}

	@PatchMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody ProyectoDto dto, BindingResult result, @PathVariable(ID_PROYECTO) Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		} else if (service.findProyecto(id) == null) {
			return respondNotFound(id.toString());
		}
		Proyecto project = service.buildEntity(dto);
		project.setIdProyecto(id);
		project = service.saveProyecto(project);
		String descripcion = String.format(RESULT_UPDATED, project.toString(), id);
		auditManager.saveAudit("correo@correo.com", ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, project, ""), HttpStatus.CREATED);
	}

	@DeleteMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PROYECTO) Long id) {
		try {
			service.deleteProyecto(id);
			ApiResponse response = buildDeleted("Proyecto", id.toString());
			// TODO agregar auditoria de que se eliminó proyecto
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Proyecto",
					id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

}
