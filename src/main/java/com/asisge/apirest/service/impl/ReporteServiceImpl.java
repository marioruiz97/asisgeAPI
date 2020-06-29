package com.asisge.apirest.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.service.IActividadService;
import com.asisge.apirest.service.IEtapaPlanService;
import com.asisge.apirest.service.IPlanTrabajoService;
import com.asisge.apirest.service.IReporteService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReporteServiceImpl implements IReporteService {

	@Autowired
	private IPlanTrabajoService planService;

	@Autowired
	private IEtapaPlanService etapaService;

	@Autowired
	private IActividadService actividadService;

	@Override
	public void generarReporteEtapas(Long idProyecto) throws FileNotFoundException, JRException {

		List<PlanDeTrabajo> planes = planService.findPlanesByProyecto(idProyecto);
		if (planes.isEmpty())
			throw new InvalidProcessException(HttpStatus.NOT_FOUND);

		Proyecto proyecto = planes.get(0).getProyecto();
		List<EtapaPDT> etapas = new ArrayList<>();
		planes.forEach(plan -> {
			etapas.addAll(etapaService.findEtapasByPlan(plan.getIdPlanDeTrabajo()));
		});

		File file = ResourceUtils.getFile("archivo.jrxml");
		JasperReport jasper = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(etapas);
	}

}
