package com.asisge.apirest.controller.actividades;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.ProyectosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.dto.actividades.TiempoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.terceros.Usuario;
import com.asisge.apirest.repository.ISeguimientoDao;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.IMiembrosService;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IUsuarioService;

@RestController
public class TiemposController extends BaseController {

	private static final String ID_PLAN = "idPlan";

	@Autowired
	private ISeguimientoDao repository;

	@Autowired
	private IPlanTrabajoService planService;

	@Autowired
	private IActividadService actividadService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMiembrosService miembroService;
	

	@GetMapping(ProyectosPath.TIEMPOS)
	public ResponseEntity<ApiResponse> getTiempos(HttpServletRequest request, @PathVariable(ID_PLAN) Long idPlan) {
		PlanDeTrabajo plan = planService.findPlanById(idPlan);
		List<Actividad> actividadesPlan = actividadService.findActividadesByPlan(plan);
		List<TiempoDto> table = new ArrayList<>();
		if (!actividadesPlan.isEmpty()) {
			if (request.isUserInRole("ROLE_ADMIN")) {
				List<Usuario> miembros = miembroService.findUsuariosByProyecto(plan.getProyecto().getIdProyecto());
				miembros.forEach(usuario ->
					actividadesPlan.forEach(actividad -> {
						List<Seguimiento> seguimiento = repository.findByActividadAsociadaAndUsuarioSeguimiento(actividad, usuario);
						if(!seguimiento.isEmpty()) 
							table.add(new TiempoDto(usuario, actividad, seguimiento));
					}));
			} else {
				Usuario usuario = usuarioService.findUsuarioByCorreo(getCurrentEmail());
				actividadesPlan.forEach(actividad -> {
					List<Seguimiento> seguimiento = repository.findByActividadAsociadaAndUsuarioSeguimiento(actividad, usuario);
					if(!seguimiento.isEmpty()) 
						table.add(new TiempoDto(usuario, actividad, seguimiento));
				});
			}
		}
		if (table.isEmpty())
			return respondNotFound(null);
		
		return new ResponseEntity<>(buildOk(table), HttpStatus.OK);
	}

}
