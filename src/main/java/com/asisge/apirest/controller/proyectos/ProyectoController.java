package com.asisge.apirest.controller.proyectos;

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
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.IProyectoService;

@RestController
public class ProyectoController extends BaseController {

	private static final String ID_PROYECTO = "idProyecto";

	@Autowired
	private IProyectoService service;
	
	@Autowired
	private IMiembrosService miembroService;

	@GetMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> findAll(HttpServletRequest request) {
		List<Proyecto> proyectos;
		if(request.isUserInRole("ROLE_ADMIN")) {
			proyectos = service.findAllProyectos();
		} else if(isAuthenticated()) {
			proyectos = miembroService.findProyectosByEmail(getCurrentEmail());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}			
		if (proyectos.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(proyectos), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> findById(HttpServletRequest request, @PathVariable(ID_PROYECTO) Long id) {
		Proyecto proyecto = null;
		String email = getCurrentEmail();
		if(request.isUserInRole("ROLE_ADMIN") || miembroService.existsMiembroInProyecto(id, email)) {
			proyecto = service.findProyecto(id);
		}
		if (proyecto == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(proyecto), HttpStatus.OK);
	}

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@PostMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProyectoDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Proyecto newProject = service.buildEntity(dto);
		newProject = service.saveProyecto(newProject);
		String descripcion = String.format(RESULT_CREATED, newProject.toString(), newProject.getIdProyecto());
		auditManager.saveAudit(newProject.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newProject, ""), HttpStatus.CREATED);
	}

	// TODO verificar si no hay que quitar este metodo
	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
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
		auditManager.saveAudit(project.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, project, ""), HttpStatus.CREATED);
	}

	@Secured({"ROLE_ADMIN"})
	@DeleteMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PROYECTO) Long id) {		
			service.deleteProyecto(id);
			ApiResponse response = buildDeleted("Proyecto", id.toString());
			auditManager.saveAudit(ACTION_DELETE, response.getMessage());
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
