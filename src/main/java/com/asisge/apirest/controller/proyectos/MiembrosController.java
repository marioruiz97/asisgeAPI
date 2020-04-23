package com.asisge.apirest.controller.proyectos;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.MiembroDto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.service.IMiembrosService;

@RestController
public class MiembrosController extends BaseController {

	public static final String ID_PROYECTO = "idProyecto";

	@Autowired
	private IMiembrosService service;

	private ResponseEntity<ApiResponse> respondRequest(List<?> listado) {
		if (listado.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(listado), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> findMiembros(@PathVariable(ID_PROYECTO) Long id, @RequestParam Optional<Boolean> withRol) {
		List<?> listado;
		if (withRol.orElse(false)) {
			listado = service.findMiembrosProyecto(id).stream().map(member -> {
				member.setProyecto(null);
				return member;
			}).collect(Collectors.toList());
			// otra opcion listado.forEach(member-> member.setProyecto(null))
		} else {
			listado = service.findUsuariosByProyecto(id);
		}
		return respondRequest(listado);
	}

	@GetMapping(ProyectosPath.MIEMBRO_PROYECTOS)
	public ResponseEntity<ApiResponse> findProyectos(@PathVariable("idUsuario") Long id) {
		List<Proyecto> proyectos = service.findProyectosByUsuario(id);
		return respondRequest(proyectos);
	}

	@PatchMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> saveMiembros(@Valid @RequestBody List<MiembroDto> dtoList, BindingResult result) {
		if (result.hasErrors())
			return validateDto(result);
		List<MiembroProyecto> listado = service.saveAll(dtoList);
		return new ResponseEntity<>(buildSuccess(RESULT_CREATED, listado, "Listado de miembros", "de proyecto: " + dtoList.get(0).getProyecto()), HttpStatus.CREATED);
	}

	
	
	@Deprecated
	@PostMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> saveMiembro(@Valid @RequestBody MiembroDto dto, BindingResult result, @PathVariable(ID_PROYECTO) Long idProyecto) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		dto.setProyecto(idProyecto);
		MiembroProyecto miembro = service.buildEntity(dto);
		miembro = service.saveMiembro(miembro);
		return new ResponseEntity<>(
				buildSuccess(RESULT_CREATED, miembro, "Miembro proyecto", miembro.getIdMiembroProyecto().toString()),
				HttpStatus.CREATED);
	}

	@Deprecated
	@DeleteMapping(ProyectosPath.PROYECTO_MIEMBROS)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PROYECTO) Long proyecto, @RequestParam @NotNull Long usuario) {
		try {
			service.deleteMiembro(proyecto, usuario);
			ApiResponse response = buildDeleted("Miembro Proyecto", String.format("(proyecto: %s, usuario: %s)", proyecto, usuario));
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Miembro",
					String.format("(proyecto: %s, usuario: %s)", proyecto, usuario));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

}
