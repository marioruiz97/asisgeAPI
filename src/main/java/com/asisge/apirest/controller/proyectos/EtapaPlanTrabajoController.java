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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.EtapaDto;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.service.IEtapaPlanService;

@RestController
public class EtapaPlanTrabajoController extends BaseController {

	private static final String ID_ETAPA = "idEtapa";

	@Autowired
	private IEtapaPlanService service;

	@GetMapping(ProyectosPath.ETAPA_PLAN)
	public ResponseEntity<ApiResponse> findAll(@PathVariable("idPlan") Long id) {
		List<EtapaPDT> etapas = service.findEtapasByPlan(id);
		if (etapas.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(etapas), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable(ID_ETAPA) Long idEtapa) {
		EtapaPDT etapa = service.findEtapaById(idEtapa);
		if (etapa == null)
			return respondNotFound(idEtapa.toString());
		return new ResponseEntity<>(buildOk(etapa), HttpStatus.OK);
	}

	@PostMapping(ProyectosPath.ETAPA_PLAN)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody EtapaDto dto, BindingResult result) {
		if (result.hasErrors())
			return validateDto(result);
		EtapaPDT etapa = service.buildEtapaEntity(dto);
		etapa = service.saveEtapa(etapa);
		return new ResponseEntity<>(
				buildSuccess(RESULT_CREATED, etapa, etapa.toString(), etapa.getIdEtapaPDT().toString()),
				HttpStatus.CREATED);
	}

	@PatchMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody EtapaDto dto, BindingResult result,
			@PathVariable(ID_ETAPA) Long idEtapa) {
		if (result.hasErrors())
			return validateDto(result);
		else if (service.findEtapaById(idEtapa) == null)
			return respondNotFound(idEtapa.toString());

		EtapaPDT etapa = service.buildEtapaEntity(dto);
		etapa.setIdEtapaPDT(idEtapa);
		etapa = service.saveEtapa(etapa);
		return new ResponseEntity<>(
				buildSuccess(RESULT_UPDATED, etapa, etapa.toString(), etapa.getIdEtapaPDT().toString()),
				HttpStatus.CREATED);
	}

	@DeleteMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_ETAPA) Long id, @RequestParam String email) {
		try {
			service.deleteEtapa(id);
			ApiResponse response = buildDeleted("Etapa Plan De trabajo", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(email, ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Etapa Plan",
					id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

}
