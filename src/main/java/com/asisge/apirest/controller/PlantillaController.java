package com.asisge.apirest.controller;

import java.util.List;

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

import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.proyectos.PlantillaPlanDto;
import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;
import com.asisge.apirest.service.IPlantillaService;

@RestController
public class PlantillaController extends BaseController {

	@Autowired
	private IPlantillaService service;

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@GetMapping(MaestrosPath.PLANTILLA)
	public ResponseEntity<ApiResponse> getPlantillas() {
		List<PlantillaPlanTrabajo> plantillas = service.findPlantillas();
		if (plantillas.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(plantillas), HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@GetMapping(MaestrosPath.PLANTILLA_ID)
	public ResponseEntity<ApiResponse> getPlantillaById(@PathVariable("idPlantilla") Long id) {
		PlantillaPlanTrabajo plantilla = service.findPlantillaById(id);
		if (plantilla == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(plantilla), HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(MaestrosPath.PLANTILLA)
	public ResponseEntity<ApiResponse> createPlantilla(@Valid @RequestBody PlantillaPlanDto dto, BindingResult result) {
		if (result.hasErrors())
			return validateDto(result);
		PlantillaPlanTrabajo plantilla = dto.getPlantilla();
		plantilla = service.savePlantilla(plantilla);
		return new ResponseEntity<>(
				buildSuccess(RESULT_CREATED, plantilla, plantilla.toString(), plantilla.getIdPlantilla().toString()),
				HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PatchMapping(MaestrosPath.PLANTILLA_ID)
	public ResponseEntity<ApiResponse> updatePlantilla(@Valid @RequestBody PlantillaPlanDto dto, BindingResult result, @PathVariable("idPlantilla") Long id) {
		if (result.hasErrors())
			return validateDto(result);
		PlantillaPlanTrabajo old = service.findPlantillaById(id);
		if (old == null)
			return respondNotFound(id.toString());
		PlantillaPlanTrabajo plantilla = dto.getPlantilla();
		plantilla.setIdPlantilla(id);
		plantilla = service.savePlantilla(plantilla);
		return new ResponseEntity<>(
				buildSuccess(RESULT_CREATED, plantilla, plantilla.toString(), plantilla.getIdPlantilla().toString()),
				HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@DeleteMapping(MaestrosPath.PLANTILLA_ID)
	public ResponseEntity<ApiResponse> deletePlantilla(@PathVariable("idPlantilla") Long id) {
		try {
			PlantillaPlanTrabajo old = service.findPlantillaById(id);
			if (old == null)
				return respondNotFound(id.toString());
			service.deletePlantillaById(id);
			return new ResponseEntity<>(buildDeleted(id.toString()), HttpStatus.OK);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Plantilla", id.toString());
			return new ResponseEntity<>(buildFail(message), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
