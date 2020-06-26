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
import com.asisge.apirest.model.dto.proyectos.CierreDto;
import com.asisge.apirest.model.dto.proyectos.EtapaDto;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.proyectos.Cierre;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.TipoCierre;
import com.asisge.apirest.service.ICierreService;
import com.asisge.apirest.service.IEtapaPlanService;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IPlanTrabajoService;

@RestController
public class EtapaPlanTrabajoController extends BaseController {

	private static final String ID_ETAPA = "idEtapa";

	@Autowired
	private IEtapaPlanService service;

	@Autowired
	private IPlanTrabajoService planService;
	
	@Autowired
	private INotificacionService notificationService;
	
	@Autowired
	private ICierreService cierreService;

	@GetMapping(ProyectosPath.ETAPA_PLAN)
	public ResponseEntity<ApiResponse> findByPlan(@PathVariable("idPlan") Long id) {
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

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.ETAPA_PLAN)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody EtapaDto dto, BindingResult result, @PathVariable("idPlan") Long idPlan) {
		if (result.hasErrors())
			return validateDto(result);
		dto.setPlanDeTrabajo(idPlan);
		EtapaPDT etapa = service.buildEtapaEntity(dto);
		etapa = service.saveEtapa(etapa);
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		cierreService.validarCierrePlan(plan);
		if (service.findEtapasByPlan(idPlan).isEmpty()) {			
			plan.setEtapaActual(etapa);
			planService.savePlan(plan);
		}
		String descripcion = String.format(RESULT_CREATED, etapa.toString(), etapa.getIdEtapaPDT());
		auditManager.saveAudit(etapa.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, etapa), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> setEtapaActual(@PathVariable("idPlan") Long idPlan, @PathVariable(ID_ETAPA) Long idEtapa) {		
		try {
			PlanDeTrabajo plan = planService.findPlanById(idPlan);
			EtapaPDT etapaActual = service.findEtapaById(idEtapa);
			cierreService.validarCierreEtapa(etapaActual);
			plan.setEtapaActual(etapaActual);
			plan = planService.savePlan(plan);
			String descripcion = String.format(Messages.getString("notification.set.etapa-plan"),
					plan.getIdPlanDeTrabajo(), etapaActual.getNombreEtapa());
			auditManager.saveAudit(plan.getLastModifiedBy(), ACTION_UPDATE, descripcion);
			notificationService.notificarUsuariosProyectos(plan.getProyecto(), descripcion, ColorNotificacion.PRIMARY);
			return new ResponseEntity<>(buildMessage(descripcion), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = Messages.getString("message.error.set-etapa-actual");
			return new ResponseEntity<>(buildFail(message), HttpStatus.BAD_REQUEST);
		}
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PatchMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody EtapaDto dto, BindingResult result, @PathVariable(ID_ETAPA) Long idEtapa) {
		if (result.hasErrors())
			return validateDto(result);
		EtapaPDT old = service.findEtapaById(idEtapa);
		if (old == null)
			return respondNotFound(idEtapa.toString());
		cierreService.validarCierreEtapa(old);
		EtapaPDT etapa = service.buildEtapaEntity(dto);		
		etapa.setIdEtapaPDT(idEtapa);
		if(old.getCierre() != null)
			etapa.setCierre(old.getCierre());
		etapa = service.saveEtapa(etapa);
		String descripcion = String.format(RESULT_UPDATED, etapa.toString(), etapa.getIdEtapaPDT());
		auditManager.saveAudit(etapa.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, etapa), HttpStatus.CREATED);
	}
	
	@PostMapping(ProyectosPath.CIERRE_ETAPAS)
	public ResponseEntity<ApiResponse> cerrarEtapa(@Valid @RequestBody CierreDto dto, BindingResult result, @PathVariable(ID_ETAPA) Long idEtapa) {
		if (result.hasErrors())
			return validateDto(result);
		EtapaPDT old = service.findEtapaById(idEtapa);
		if (old == null)
			return respondNotFound(idEtapa.toString());
		Cierre cierre = null;
		if (old.getCierre() == null) {
			cierre = new Cierre(null, dto.getObservaciones(), dto.getAvalCliente(), TipoCierre.CIERRE_ETAPA);
		} else {
			cierre = old.getCierre();
			cierre.setObservaciones(dto.getObservaciones());
			cierre.setAvalCliente(dto.getAvalCliente());
		}
		cierre = cierreService.saveCierre(cierre);
		old.setCierre(cierre);
		service.saveEtapa(old);
		String descripcion = String.format("Se ha guardado el cierre (%s) para la etapa con id %s", cierre.toString(), old.getIdEtapaPDT());
		auditManager.saveAudit(ACTION_CREATE, descripcion);		
		return new ResponseEntity<>(buildSuccess(descripcion, cierre), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@DeleteMapping(ProyectosPath.ETAPA_PLAN_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_ETAPA) Long id) {
		EtapaPDT old = service.findEtapaById(id);
		cierreService.validarCierreEtapa(old);
		service.deleteEtapa(id);
		ApiResponse response = buildDeleted("Etapa Plan De trabajo", id.toString());
		String descripcion = response.getMessage();
		auditManager.saveAudit(ACTION_DELETE, descripcion);
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
