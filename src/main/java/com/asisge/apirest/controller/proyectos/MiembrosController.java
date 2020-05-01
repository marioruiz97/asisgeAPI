package com.asisge.apirest.controller.proyectos;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.MiembroDto;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IProyectoService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class MiembrosController extends BaseController {

	public static final String ID_PROYECTO = "idProyecto";

	@Autowired
	private IMiembrosService service;

	@Autowired
	private IProyectoService projectService;

	@Autowired
	private IUsuarioService userService;

	@Autowired
	private INotificacionService notificationService;

	private ResponseEntity<ApiResponse> respondRequest(List<?> listado) {
		if (listado.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(listado), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.MIEMBRO_PROYECTOS)
	public ResponseEntity<ApiResponse> findProyectos(@PathVariable("idUsuario") Long id) {
		List<Proyecto> proyectos = service.findProyectosByUsuario(id);
		return respondRequest(proyectos);
	}

	@GetMapping(ProyectosPath.POSIBLES_MIEMBROS)
	public ResponseEntity<ApiResponse> findPosiblesMiembros(@PathVariable("id") Long idProyecto) {
		List<Usuario> proyectos = service.findPosiblesMiembros(idProyecto);
		return respondRequest(proyectos);
	}

	@PostMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> saveMiembro(@Valid @RequestBody MiembroDto dto, BindingResult result, @PathVariable(ID_PROYECTO) Long idProyecto) {
		if (result.hasErrors())
			return validateDto(result);
		dto.setProyecto(idProyecto);
		MiembroProyecto miembro = service.buildEntity(dto);
		miembro.setProyecto(projectService.findProyectoById(dto.getProyecto()));
		miembro.setUsuario(userService.findUsuarioById(dto.getUsuario()));
		miembro = service.saveMiembro(miembro);
		String added = String.format(Messages.getString("notification.added.member"), 
				miembro.getUsuario().getCorreo(), miembro.getProyecto().getNombreProyecto());
		notificationService.notificarUsuariosProyectos(miembro.getProyecto(), added, ColorNotificacion.PRIMARY);
		ApiResponse response = buildSuccess(RESULT_CREATED, miembro, "Miembro proyecto", miembro.getIdMiembroProyecto().toString());
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PROYECTO) Long proyecto, @RequestParam @NotNull Long miembro) {
		try {
			service.deleteMiembro(miembro);
			ApiResponse response = buildDeleted("Miembro Proyecto", miembro.toString());
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Miembro", miembro);
			return new ResponseEntity<>(buildFail(message), HttpStatus.BAD_REQUEST);
		}
	}

}
