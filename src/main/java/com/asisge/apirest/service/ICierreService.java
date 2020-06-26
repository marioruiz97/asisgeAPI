package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.proyectos.Cierre;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;

public interface ICierreService {

	Cierre saveCierre(Cierre cierre);

	List<Cierre> findAll();

	void validarCierrePlan(PlanDeTrabajo plan);

	void validarCierreEtapa(EtapaPDT etapa);

}
