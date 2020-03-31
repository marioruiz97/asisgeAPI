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
import com.asisge.apirest.model.dto.proyectos.PlanTrabajoDto;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.service.IPlanTrabajoService;

@RestController
public class PlanTrabajoController extends BaseController {

	public static final String ID_PLAN = "idPlan";

	@Autowired
	private IPlanTrabajoService service;

	@GetMapping(ProyectosPath.PLANES_TRABAJO)
	public ResponseEntity<ApiResponse> findAll() {
		List<PlanDeTrabajo> planes = service.findAllPlanes();
		if (planes.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(planes), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.PLAN_TRABAJO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable(ID_PLAN) Long id) {
		PlanDeTrabajo plan = service.findPlanById(id);
		if (plan == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(plan), HttpStatus.OK);
	}

	@PostMapping(ProyectosPath.PLANES_TRABAJO)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody PlanTrabajoDto dto, BindingResult result) {
		if (result.hasErrors())
			return validateDto(result);
		PlanDeTrabajo newPlan = service.buildPlanEntity(dto);
		newPlan = service.savePlan(newPlan);
		return new ResponseEntity<>(
				buildSuccess(RESULT_CREATED, newPlan, newPlan.toString(), newPlan.getIdPlanDeTrabajo().toString()),
				HttpStatus.CREATED);
	}

	@PatchMapping(ProyectosPath.PLAN_TRABAJO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody PlanTrabajoDto dto, BindingResult result,
			@PathVariable(ID_PLAN) Long id) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		PlanDeTrabajo old = service.findPlanById(id);
		if (old == null) {
			return respondNotFound(id.toString());
		}
		PlanDeTrabajo plan = service.buildPlanEntity(dto);
		plan.setIdPlanDeTrabajo(id);
		plan.setEtapas(old.getEtapas());
		plan = service.savePlan(plan);
		return new ResponseEntity<>(
				buildSuccess(RESULT_UPDATED, plan, plan.toString(), plan.getIdPlanDeTrabajo().toString()),
				HttpStatus.CREATED);
	}

	@DeleteMapping(ProyectosPath.PLAN_TRABAJO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PLAN) Long id, @RequestParam String email) {
		try {
			service.deletePlan(id);
			ApiResponse response = buildDeleted("Plan De trabajo", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(email, ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "Plan de trabajo",
					id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}

}
