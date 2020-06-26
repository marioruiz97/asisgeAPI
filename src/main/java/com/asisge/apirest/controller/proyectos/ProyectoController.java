package com.asisge.apirest.controller.proyectos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.proyectos.Dashboard;
import com.asisge.apirest.model.dto.proyectos.ProyectoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.ColorNotificacion;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.IEstadoProyectoService;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.INotificacionService;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IProyectoService;
import com.asisge.apirest.service.IUploadFileService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class ProyectoController extends BaseController {

	private static final String ID_PROYECTO = "idProyecto";
	private static final String ROLE_ADMIN = "ROLE_ADMIN";

	@Autowired
	private IProyectoService service;

	@Autowired
	private IMiembrosService miembroService;

	@Autowired
	private IUsuarioService userService;

	@Autowired
	private INotificacionService notificacionService;

	@Autowired
	private IEstadoProyectoService estadosService;
	
	@Autowired
	private IPlanTrabajoService planService;
	
	@Autowired
	private IActividadService actividadService;
	
	@Autowired
	private IUploadFileService fileService;

	@GetMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> findAll(HttpServletRequest request) {
		List<Proyecto> proyectos;
		if (request.isUserInRole(ROLE_ADMIN)) {
			proyectos = service.findAllProyectos();
		} else if (isAuthenticated()) {
			proyectos = miembroService.findProyectosByEmail(getCurrentEmail());
		} else {
			throw new InvalidProcessException(HttpStatus.FORBIDDEN);
		}
		if (proyectos.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(proyectos), HttpStatus.OK);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@GetMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> findProyectoById(HttpServletRequest request, @PathVariable(ID_PROYECTO) Long idProyecto) {
		String email = getCurrentEmail();
		Proyecto proyecto = null;
		if (request.isUserInRole(ROLE_ADMIN) || miembroService.existsMiembroInProyecto(idProyecto, email)) {
			proyecto = service.findProyectoById(idProyecto);
		}
		if (proyecto == null)
			return respondNotFound(idProyecto.toString());
		return new ResponseEntity<>(buildOk(proyecto), HttpStatus.OK);
	}

	@GetMapping(ProyectosPath.DASHBOARD)
	public ResponseEntity<ApiResponse> findDashboardById(HttpServletRequest request, @PathVariable("id") Long id) {
		Dashboard dashboard = null;
		String email = getCurrentEmail();
		if (request.isUserInRole(ROLE_ADMIN) || miembroService.existsMiembroInProyecto(id, email)) {
			dashboard = service.loadDashboard(id);
			dashboard.setMiembros(miembroService.findMiembrosProyecto(id));
			dashboard.setNotificaciones(notificacionService.findByProyecto(dashboard.getProyecto()));
			dashboard.setLineaEstados(estadosService.findEstadosLine(dashboard.getEstadoActual()));
			dashboard.setAvance(5);
			List<PlanDeTrabajo> planes = planService.findPlanesByProyecto(id);
			if (!planes.isEmpty()) {
				List<Actividad> actividades = new ArrayList<>();
				planes.forEach(plan -> {
					List<Actividad> add = actividadService.findActividadesByPlan(plan);
					if (!add.isEmpty())
						actividades.addAll(add);
				});
				if (!actividades.isEmpty()) {
					dashboard.setAvance(service.calcAvance(actividades));
					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 7);
					Instant oneWeek = cal.getTime().toInstant();
					List<Actividad> proximas = actividades.stream().filter(act -> act.getFechaVencimiento() != null 
							&& oneWeek.compareTo(act.getFechaVencimiento().toInstant()) > 0).collect(Collectors.toList());
					dashboard.setProximasActividades(proximas);
				}
			}
		}
		if (dashboard == null)
			return respondNotFound(id.toString());
		return new ResponseEntity<>(buildOk(dashboard), HttpStatus.OK);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.PROYECTOS)
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProyectoDto dto, BindingResult result) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Proyecto newProject = service.buildEntity(dto);
		newProject = service.saveProyecto(newProject);
		MiembroProyecto miembro = new MiembroProyecto(null, userService.findUsuarioByCorreo(getCurrentEmail()), newProject, "Líder");
		miembroService.saveMiembro(miembro);
		String descripcion = String.format(RESULT_CREATED, "proyecto: " + newProject.getNombreProyecto(), newProject.getIdProyecto());
		auditManager.saveAudit(newProject.getCreatedBy(), ACTION_CREATE, descripcion);
		notificacionService.notificarProyecto(newProject, descripcion, ColorNotificacion.SUCCESS);
		String added = String.format(Messages.getString("notification.added.member"), getCurrentEmail(), newProject.getNombreProyecto());
		notificacionService.notificarUsuario(newProject, miembro.getUsuario(), added, ColorNotificacion.PRIMARY, 5);
		notificacionService.notificarAdmins(newProject.getNombreProyecto());
		return new ResponseEntity<>(buildSuccess(descripcion, newProject), HttpStatus.CREATED);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PatchMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> update(@Valid @RequestBody ProyectoDto dto, BindingResult result, @PathVariable(ID_PROYECTO) Long idProyecto) {
		if (result.hasErrors()) {
			return validateDto(result);
		}
		Proyecto old = service.findProyectoById(idProyecto);
		if(old == null)
			return respondNotFound(idProyecto.toString());
		Proyecto proyecto = service.buildEntity(dto);
		proyecto.setIdProyecto(idProyecto);
		proyecto.setContrato(old.getContrato());
		proyecto.setAnticipo(old.getAnticipo());
		proyecto = service.saveProyecto(proyecto);
		String descripcion = String.format(RESULT_UPDATED, "proyecto: " + proyecto.getNombreProyecto(), proyecto.getIdProyecto());
		auditManager.saveAudit(proyecto.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		notificacionService.notificarProyecto(proyecto, descripcion, ColorNotificacion.SUCCESS);
		return new ResponseEntity<>(buildSuccess(descripcion, proyecto), HttpStatus.CREATED);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PostMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> changeEstado(@PathVariable(ID_PROYECTO) Long idProyecto, @NotNull @RequestParam("estado") Long idEstado) {
		Proyecto proyecto = service.findProyectoById(idProyecto);
		if (proyecto == null) {
			return respondNotFound(idProyecto.toString());
		}
		EstadoProyecto estado = estadosService.findEstadoById(idEstado);
		proyecto.setEstadoProyecto(estado);
		proyecto = service.saveProyecto(proyecto);
		String descripcion = Messages.getString("notification.new-project-state");
		String mensaje = String.format(descripcion, proyecto.getNombreProyecto(), estado.getNombreEstado());
		auditManager.saveAudit(Messages.getString("message.action.project-change-status"), mensaje);
		notificacionService.notificarUsuariosProyectos(proyecto, mensaje, ColorNotificacion.SUCCESS);
		return new ResponseEntity<>(buildSuccess(mensaje, proyecto), HttpStatus.CREATED);
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_ASESOR" })
	@PutMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> uploadArchivos(@RequestParam("archivo") MultipartFile archivo, @RequestParam("contrato") boolean contrato, 
			@PathVariable(ID_PROYECTO) Long idProyecto) {
		Proyecto proyecto = service.findProyectoById(idProyecto);
		if (proyecto == null)
			return respondNotFound(idProyecto.toString());
		try {
			if (!archivo.isEmpty()) {
				String rutaArchivo = fileService.cargarContratoOAnticipo(archivo, proyecto.getIdProyecto());
				if (contrato) {
					proyecto.setContrato(rutaArchivo);
				} else {
					proyecto.setAnticipo(rutaArchivo);
				}
				proyecto = service.saveProyecto(proyecto);
				return new ResponseEntity<>(buildSuccess("Se ha cargado archivo: " + rutaArchivo, proyecto), HttpStatus.CREATED);
			}
			throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new InvalidProcessException("No se ha podido cargar archivo", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Secured({ "ROLE_ADMIN" })
	@DeleteMapping(ProyectosPath.PROYECTO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable(ID_PROYECTO) Long id) {
		service.deleteProyecto(id);
		ApiResponse response = buildDeleted("Proyecto", id.toString());
		auditManager.saveAudit(ACTION_DELETE, response.getMessage());
		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}

}
