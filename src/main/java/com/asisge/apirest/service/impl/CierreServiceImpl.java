package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.asisge.apirest.model.entity.proyectos.Cierre;
import com.asisge.apirest.repository.ICierreDao;
import com.asisge.apirest.service.ICierreService;

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

}
