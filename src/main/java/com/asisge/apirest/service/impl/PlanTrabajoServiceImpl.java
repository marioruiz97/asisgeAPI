package com.asisge.apirest.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.dto.proyectos.EtapaDto;
import com.asisge.apirest.model.dto.proyectos.PlanTrabajoDto;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.repository.IEtapaPlanDao;
import com.asisge.apirest.repository.IPlanDeTrabajoDao;
import com.asisge.apirest.service.IEtapaPlanService;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IProyectoService;

@Service
public class PlanTrabajoServiceImpl implements IPlanTrabajoService, IEtapaPlanService {

	@Autowired
	private IPlanDeTrabajoDao planDao;

	@Autowired
	private IEtapaPlanDao etapaDao;

	@Autowired
	private IProyectoService proyectoService;

	@Override
	public EtapaPDT saveEtapa(EtapaPDT etapa) {
		return etapaDao.save(etapa);
	}

	@Override
	public List<EtapaPDT> saveAllEtapas(List<EtapaDto> dtoList) {
		List<EtapaPDT> etapas = dtoList.stream().map(dto -> {
			PlanDeTrabajo plan = findPlanById(dto.getPlanDeTrabajo());
			return new EtapaPDT(null, plan, dto.getNombreEtapa(), dto.getFechaInicio(), dto.getFechaFin());
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
		etapaDao.deleteById(id);
	}

	@Override
	public PlanDeTrabajo savePlan(PlanDeTrabajo plan) {
		return planDao.save(plan);
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
		planDao.deleteById(id);
	}

	@Override
	public PlanDeTrabajo buildPlanEntity(PlanTrabajoDto dto) {
		EtapaPDT etapa = dto.getEtapaActual() != null ? findEtapaById(dto.getEtapaActual()) : null;
		Proyecto proyecto = proyectoService.findProyectoById(dto.getProyecto());
		return new PlanDeTrabajo(null, dto.getFechaInicio(), dto.getFechaFinEstimada(), dto.getFechaFinReal(),
				dto.getHorasMes(), dto.getObjetivoPlan(), proyecto, null, etapa);
	}

	@Override
	public EtapaPDT buildEtapaEntity(EtapaDto dto) {
		PlanDeTrabajo plan = new PlanDeTrabajo();
		plan.setIdPlanDeTrabajo(dto.getPlanDeTrabajo());
		return new EtapaPDT(null, plan, dto.getNombreEtapa(), dto.getFechaInicio(), dto.getFechaFin());
	}

}
