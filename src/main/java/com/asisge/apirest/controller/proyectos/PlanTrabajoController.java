package com.asisge.apirest.controller.proyectos;

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

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.PlanTrabajoDto;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IPlanTrabajoService;

@RestController
public class PlanTrabajoController extends BaseController {

	public static final String ID_PLAN = "idPlan";

	@Autowired
	private IPlanTrabajoService service;

	@Autowired
	private INotificacionService notificacionService;

	@GetMapping(ProyectosPath.PLANES_TRABAJO)
	public ResponseEntity<ApiResponse> findPlanByProyecto(@PathVariable("idProyecto") Long idProyecto) {
		List<PlanDeTrabajo> planes = service.findPlanesByProyecto(idProyecto);
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

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.PLANES_TRABAJO)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody PlanTrabajoDto dto, BindingResult result,
			@PathVariable("idProyecto") Long idProyecto) {
		if (result.hasErrors())
			return validateDto(result);
		dto.setProyecto(idProyecto);
		PlanDeTrabajo newPlan = service.buildPlanEntity(dto);
		newPlan = service.savePlan(newPlan);
		String descripcion = String.format(RESULT_CREATED, newPlan.toString(), newPlan.getIdPlanDeTrabajo());
		auditManager.saveAudit(newPlan.getCreatedBy(), ACTION_CREATE, descripcion);
		String notificacion = String.format(Messages.getString("notification.added.plan"), newPlan.getCreatedBy(),
				idProyecto);
		notificacionService.notificarUsuariosProyectos(newPlan.getProyecto(), notificacion, ColorNotificacion.SUCCESS);
		return new ResponseEntity<>(buildSuccess(notificacion, newPlan), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
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
		String descripcion = String.format(RESULT_UPDATED, plan.toString(), plan.getIdPlanDeTrabajo());
		auditManager.saveAudit(plan.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, plan, ""), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@DeleteMapping(ProyectosPath.PLAN_TRABAJO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PLAN) Long id) {
		service.deletePlan(id);
		ApiResponse response = buildDeleted("Plan De trabajo", id.toString());
		String descripcion = response.getMessage();
		auditManager.saveAudit(ACTION_DELETE, descripcion);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
