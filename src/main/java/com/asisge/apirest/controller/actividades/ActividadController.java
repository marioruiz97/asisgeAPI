package com.asisge.apirest.controller.actividades;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.actividades.ActividadDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.IEstadoActividadService;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IPlanTrabajoService;

@RestController
public class ActividadController extends BaseController {

	private static final String ID_ACTIVIDAD = "idActividad";
	private static final String ID_PLAN = "idPlan";

	@Autowired
	private IActividadService service;

	@Autowired
	private IPlanTrabajoService planService;

	@Autowired
	private INotificacionService notificationService;
	
	@Autowired
	private IEstadoActividadService estadoActividadService;
	
	@Autowired
	private IMiembrosService miembroService;

	@GetMapping(ProyectosPath.ACTIVIDADES_PLAN)
	public ResponseEntity<ApiResponse> getActividadesByPlan(@PathVariable(ID_PLAN) Long idPlan) {
		List<Actividad> actividades = new ArrayList<>();
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		if (plan != null) {
			plan.getEtapas().forEach(etapa -> {
				List<Actividad> actividadesEtapa = service.findActividadesByEtapa(etapa.getIdEtapaPDT());
				actividades.addAll(actividadesEtapa);
			});
		}
		return new ResponseEntity<>(buildOk(actividades), actividades.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.ACTIVIDAD_ID)
	public ResponseEntity<ApiResponse> getActividadById(@PathVariable(ID_ACTIVIDAD) Long idActividad) {
		Actividad actividad = service.findActividadById(idActividad);
		if (actividad == null)
			return respondNotFound(idActividad.toString());
		return new ResponseEntity<>(buildOk(actividad), HttpStatus.OK);
	}

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@PostMapping(ProyectosPath.ACTIVIDADES_PLAN)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ActividadDto dto, BindingResult result, @PathVariable(ID_PLAN) Long idPlan) {
		if (result.hasErrors())
			return validateDto(result);
		Proyecto proyecto = planService.findPlanById(idPlan).getProyecto();
		if (proyecto != null) {
			Actividad actividad = service.buildActividad(dto);
			service.setResponsables(actividad, dto.getResponsables());
			actividad.setEstadoActividad(estadoActividadService.findEstadoInicial());
			actividad = service.saveActividad(actividad);
			String mensajeAuditoria = String.format(Messages.getString("notification.added.activity"), getCurrentEmail(), actividad.getNombre());
			auditManager.saveAudit(actividad.getCreatedBy(), ACTION_CREATE, mensajeAuditoria);
			notificationService.notificarProyecto(proyecto, mensajeAuditoria, ColorNotificacion.SUCCESS);
			String notificacion = String.format(Messages.getString("notification.added.responsable"), actividad.getIdActividad(), actividad.getNombre());
			notificationService.notificarResponsablesActividad(dto.getResponsables(), notificacion);
			return new ResponseEntity<>(buildSuccess(mensajeAuditoria, actividad), HttpStatus.CREATED);
		}
		throw new InvalidProcessException(Messages.getString("message.error.project-not-found"), HttpStatus.BAD_REQUEST);
	}

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@PatchMapping(ProyectosPath.ACTIVIDADES_PLAN_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody ActividadDto dto, BindingResult result, @PathVariable(ID_PLAN) Long idPlan, @PathVariable(ID_ACTIVIDAD) Long idActividad) {
		if (result.hasErrors())
			return validateDto(result);
		Proyecto proyecto = planService.findPlanById(idPlan).getProyecto();
		if (proyecto != null && service.findActividadById(idActividad) != null) {
			Actividad actividad = service.buildActividad(dto);
			if (actividad.getEstadoActividad().getActividadCompletada().booleanValue())
				throw new InvalidProcessException(Messages.getString("message.error.update-finished-actividad"), HttpStatus.BAD_REQUEST);
			actividad.setIdActividad(idActividad);
			service.setResponsables(actividad, dto.getResponsables());
			actividad = service.saveActividad(actividad);
			String mensaje = String.format(RESULT_UPDATED, "Actividad " + actividad.getNombre(), actividad.getIdActividad());
			auditManager.saveAudit(actividad.getLastModifiedBy(), ACTION_UPDATE, mensaje);
			notificationService.notificarProyecto(proyecto, mensaje, ColorNotificacion.SUCCESS);
			return new ResponseEntity<>(buildSuccess(mensaje, actividad), HttpStatus.CREATED);
		}
		throw new InvalidProcessException(Messages.getString("message.error.project-not-found"), HttpStatus.BAD_REQUEST);
	}
	
	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@PatchMapping(ProyectosPath.ACTIVIDAD_ID)
	public ResponseEntity<ApiResponse> changeEstado(@PathVariable(ID_ACTIVIDAD) Long idActividad, HttpServletRequest request, 
			@RequestParam("plan") Long idPlan, @RequestParam("aprobar") Boolean aprobar, @RequestParam("nuevo-estado") Long idNuevoEstado) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		if (plan == null)
			throw new InvalidProcessException(HttpStatus.BAD_REQUEST);

		Long idProyecto = plan.getProyecto().getIdProyecto();
		if (!miembroService.existsMiembroInProyecto(idProyecto, getCurrentEmail()) && !request.isUserInRole("ROLE_ADMIN"))
			throw new InvalidProcessException(HttpStatus.FORBIDDEN);

		Actividad actividad = service.findActividadById(idActividad);
		if (actividad == null)
			return respondNotFound(idActividad.toString());
		if (aprobar.booleanValue()) {
			service.solicitarAprobacion(actividad);
		} else {
			actividad = service.changeEstadoActividad(request, actividad, idNuevoEstado);
		}
		return new ResponseEntity<>(buildOk(actividad), HttpStatus.ACCEPTED);
	}

	@Secured({"ROLE_ADMIN", "ROLE_ASESOR"})
	@DeleteMapping(ProyectosPath.ACTIVIDADES_PLAN_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PLAN) Long idPlan, @PathVariable(ID_ACTIVIDAD) Long idActividad) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		if (plan != null) {
			List<Actividad> actividadesPlan = service.findActividadesByPlan(plan);
			boolean existeEnPlan = actividadesPlan.stream().anyMatch(actividad-> actividad.getIdActividad().equals(idActividad));
			if(existeEnPlan) {
				service.deleteActividad(idActividad);
				ApiResponse response = buildDeleted("Actividad", idActividad.toString());
				String descripcion = response.getMessage();
				auditManager.saveAudit(ACTION_DELETE, descripcion);
				return new ResponseEntity<>(response, HttpStatus.ACCEPTED);								
			}
		}
		String mensaje = Messages.getString("message.error.actividad-not-found");
		return new ResponseEntity<>(buildFail(mensaje), HttpStatus.NOT_FOUND);
	}

}
