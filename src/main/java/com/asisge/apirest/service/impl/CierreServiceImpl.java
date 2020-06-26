package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.asisge.apirest.config.InvalidProcessException;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.entity.proyectos.Cierre;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.proyectos.PlanDeTrabajo;
import com.asisge.apirest.repository.ICierreDao;
import com.asisge.apirest.service.ICierreService;

@Service
public class CierreServiceImpl implements ICierreService {

	@Autowired
	private ICierreDao cierreDao;
	
	@Override
	public Cierre saveCierre(Cierre cierre) {
		return cierreDao.save(cierre);
	}

	@Override
	public List<Cierre> findAll() {
		return cierreDao.findAll();
	}

	@Override
	public void validarCierrePlan(PlanDeTrabajo plan) {
		if(plan.getCierre() != null)
			throw new InvalidProcessException(Messages.getString("message.error.modify-plan-or-etapa"), HttpStatus.BAD_REQUEST);
		
	}

	@Override
	public void validarCierreEtapa(EtapaPDT etapa) {
		validarCierrePlan(etapa.getPlanDeTrabajo());
		if(etapa.getCierre() != null)
			throw new InvalidProcessException(Messages.getString("message.error.modify-plan-or-etapa"), HttpStatus.BAD_REQUEST);
	
	}

}
