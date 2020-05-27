package com.asisge.apirest.controller.proyectos;

import javax.annotation.Nullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.AprobacionPlanDto;
import com.asisge.apirest.model.entity.proyectos.AprobacionPlan;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.repository.IAprobacionDao;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IUploadFileService;

@RestController
@RequestMapping(value = MaestrosPath.APROBACIONES_PLAN)
public class AprobacionPlanController extends BaseController {

	@Autowired
	private IAprobacionDao repository;

	@Autowired
	private IPlanTrabajoService planService;
	
	@Autowired
	private IUploadFileService fileService;

	@GetMapping
	public ResponseEntity<ApiResponse> getAprobacion(@PathVariable("idPlan") Long idPlan) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		AprobacionPlan aprobacion = repository.findByPlan(plan).orElse(null);
		return new ResponseEntity<>(buildOk(aprobacion), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createAprobacion(@PathVariable("idPlan") Long idPlan, @RequestBody AprobacionPlanDto dto) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		AprobacionPlan aprobacion = new AprobacionPlan(null, plan, dto.getAvalCliente(), dto.getObservaciones(), dto.getRutaArchivo());
		aprobacion = repository.save(aprobacion);
		String mensaje = String.format(RESULT_CREATED, "Aprobación", aprobacion.getId());
		auditManager.saveAudit(ACTION_CREATE, mensaje);
		return new ResponseEntity<>(buildSuccess(mensaje, aprobacion), HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ApiResponse> updateAprobacion(@PathVariable("idPlan") Long idPlan, @PathVariable("id") Long idAprobacion, 
			@RequestBody AprobacionPlanDto dto) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		AprobacionPlan aprobacion = repository.findById(idAprobacion).orElse(null);
		if (plan == null || aprobacion == null)
			return respondNotFound(idAprobacion.toString());

		aprobacion.setAvalCliente(dto.getAvalCliente());
		aprobacion.setObservaciones(dto.getObservaciones());		
		aprobacion = repository.save(aprobacion);
		String mensaje = String.format(RESULT_UPDATED, "Aprobación", aprobacion.getId());
		auditManager.saveAudit(ACTION_UPDATE, mensaje);
		return new ResponseEntity<>(buildSuccess(mensaje, aprobacion), HttpStatus.CREATED);
	}
	
	@PutMapping
	public ResponseEntity<ApiResponse> uploadFile(@RequestParam("archivo") MultipartFile archivo, @Nullable @RequestParam("id") Long id, @PathVariable("idPlan") Long idPlan) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		if (plan == null)
			throw new InvalidProcessException("No se ha podido cargar archivo", HttpStatus.NOT_FOUND);
		try {
			if (!archivo.isEmpty()) {
				String rutaArchivo = fileService.crearArchivoProyecto(archivo, plan.getProyecto().getIdProyecto());
				if (id != null) {
					AprobacionPlan aprobacion = repository.findById(id).orElse(null);
					aprobacion.setRutaArchivo(rutaArchivo);
					repository.save(aprobacion);
				}
				return new ResponseEntity<>(buildMessage("Se ha cargado archivo: " + rutaArchivo), HttpStatus.CREATED);
			}
			throw new IllegalArgumentException();
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidProcessException("No se ha podido cargar archivo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
