package com.asisge.apirest.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.dto.proyectos.EtapaDto;
import com.asisge.apirest.model.dto.proyectos.PlanTrabajoDto;
import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.EstadoActividad;
import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.repository.IActividadDao;
import com.asisge.apirest.repository.IEtapaPlanDao;
import com.asisge.apirest.repository.IPlanDeTrabajoDao;
import com.asisge.apirest.service.IEstadoActividadService;
import com.asisge.apirest.service.IEtapaPlanService;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IPlantillaService;
import com.asisge.apirest.service.IProyectoService;

@Service
public class PlanTrabajoServiceImpl implements IPlanTrabajoService, IEtapaPlanService {

	@Autowired
	private IPlanDeTrabajoDao planDao;

	@Autowired
	private IEtapaPlanDao etapaDao;

	@Autowired
	private IActividadDao actividadDao;

	@Autowired
	private IProyectoService proyectoService;

	@Autowired
	private IPlantillaService plantillaService;

	@Autowired
	private IEstadoActividadService estadoActividadService;
	
	@Override
	public EtapaPDT saveEtapa(EtapaPDT etapa) {
		return etapaDao.save(etapa);
	}

	@Override
	public List<EtapaPDT> saveAllEtapas(List<EtapaDto> dtoList) {
		List<EtapaPDT> etapas = dtoList.stream().map(dto -> {
			PlanDeTrabajo plan = findPlanById(dto.getPlanDeTrabajo());
			return new EtapaPDT(null, plan, dto.getNombreEtapa(), dto.getFechaInicio(), dto.getFechaFin(), null);
		}).collect(Collectors.toList());
		return etapaDao.saveAll(etapas);
	}

	@Override
	public List<EtapaPDT> findAllEtapas() {
		return etapaDao.findAll(Sort.by(Direction.ASC, "fechaInicio"));
	}

	@Override
	public List<PlanDeTrabajo> findPlanesByProyecto(Long idProyecto) {
		Proyecto proyecto = proyectoService.findProyectoById(idProyecto);
		return planDao.findByProyecto(proyecto);
	}

	@Override
	public EtapaPDT findEtapaById(Long id) {
		return etapaDao.findById(id).orElse(null);
	}

	@Override
	public List<EtapaPDT> findEtapasByPlan(Long idPlan) {
		PlanDeTrabajo plan = findPlanById(idPlan);
		return etapaDao.findByPlanDeTrabajo(plan);
	}

	@Override
	public void deleteEtapa(Long id) {
		EtapaPDT etapa = findEtapaById(id);
		List<Actividad> actividades = actividadDao.findByEtapa(etapa);
		if(!actividades.isEmpty())
			throw new InvalidProcessException(Messages.getString("message.error.delete-etapa-dueto-actividades"), HttpStatus.NOT_ACCEPTABLE);
		etapaDao.deleteById(id);
	}

	@Override
	public PlanDeTrabajo savePlan(PlanDeTrabajo plan) {
		canCreatePlan(plan);
		return planDao.save(plan);
	}

	@Override
	public PlanDeTrabajo createFromTemplate(PlanDeTrabajo plan, Long idPlantilla) {
		canCreatePlan(plan);
		PlantillaPlanTrabajo plantilla = plantillaService.findPlantillaById(idPlantilla);
		if (plantilla != null) {
			List<EtapaPDT> etapas = plantilla.getEtapas().stream().map(etapa -> {
				final Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, etapa.getDuracion());
				Date fechaFin = cal.getTime();
				return new EtapaPDT(null, null, etapa.getNombreEtapa(), new Date(), fechaFin, null);
			}).collect(Collectors.toList());

			final PlanDeTrabajo newPlan = planDao.save(plan);
			etapas.forEach(etapa -> etapa.setPlanDeTrabajo(newPlan));
			etapaDao.saveAll(etapas);
			final List<EtapaPDT> planEtapas = findEtapasByPlan(newPlan.getIdPlanDeTrabajo());
			plantilla.getEtapas().stream().forEach(etapa -> {
				EtapaPDT etapaPdt = planEtapas.stream()
						.filter(et -> et.getNombreEtapa().equalsIgnoreCase(etapa.getNombreEtapa())).findFirst().orElse(null);
				EstadoActividad estadoInicial = estadoActividadService.findEstadoInicial();
				List<Actividad> actividades = etapa.getActividades().stream()
						.map(ac -> new Actividad(null, ac.getNombre(), null, etapaPdt, estadoInicial, null, ac.getDuracion(), null))
						.collect(Collectors.toList());
				actividadDao.saveAll(actividades);
			});
			return newPlan;
		}
		return plan;
	}

	private void canCreatePlan(PlanDeTrabajo plan) {
		boolean cannotCreate = planDao.findByProyecto(plan.getProyecto()).stream().anyMatch(sinTermino -> !sinTermino.equals(plan) && sinTermino.getFechaFinReal() == null);
		if (cannotCreate)
			throw new IllegalArgumentException(Messages.getString("message.error.plans-without-end"));
	}

	@Override
	public List<PlanDeTrabajo> findAllPlanes() {
		return planDao.findAll();
	}

	@Override
	public PlanDeTrabajo findPlanById(Long id) {
		return planDao.findById(id).orElse(null);
	}

	@Override
	public void deletePlan(Long id) {
		List<EtapaPDT> etapas = findEtapasByPlan(id);
		if(!etapas.isEmpty())
			throw new InvalidProcessException(Messages.getString("message.error.delete-plan-dueto-etapas"), HttpStatus.NOT_ACCEPTABLE);
		planDao.deleteById(id);
	}

	@Override
	public PlanDeTrabajo buildPlanEntity(PlanTrabajoDto dto) {
		EtapaPDT etapa = dto.getEtapaActual() != null ? findEtapaById(dto.getEtapaActual()) : null;
		Proyecto proyecto = proyectoService.findProyectoById(dto.getProyecto());
		if (proyecto.getFechaCierreProyecto().compareTo(dto.getFechaFinEstimada()) < 0) {
			String message = String.format(Messages.getString("message.error.plan-end-date"), proyecto.getFechaCierreProyecto());
			throw new InvalidProcessException(message, HttpStatus.BAD_REQUEST);
		}
		return new PlanDeTrabajo(null, dto.getFechaInicio(), dto.getFechaFinEstimada(), dto.getFechaFinReal(),
				dto.getHorasMes(), dto.getNombrePlan(), dto.getObjetivoPlan(), proyecto, null, etapa, null);
	}

	@Override
	public EtapaPDT buildEtapaEntity(EtapaDto dto) {
		PlanDeTrabajo plan = findPlanById(dto.getPlanDeTrabajo());
		if (plan.getFechaFinEstimada().compareTo(dto.getFechaFin()) < 0 || plan.getFechaInicio().compareTo(dto.getFechaInicio()) > 0) {
			throw new InvalidProcessException(Messages.getString("message.error.etapa-end-date"), HttpStatus.BAD_REQUEST);
		}
		return new EtapaPDT(null, plan, dto.getNombreEtapa(), dto.getFechaInicio(), dto.getFechaFin(), null);
	}

}
