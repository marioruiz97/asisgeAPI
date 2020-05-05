package com.asisge.apirest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;
import com.asisge.apirest.repository.IPlantillaPlanDao;
import com.asisge.apirest.service.IPlantillaService;

@Service
public class PlantillaServiceImpl implements IPlantillaService {

	@Autowired
	private IPlantillaPlanDao repository;

	@Override
	public List<PlantillaPlanTrabajo> findPlantillas() {
		return repository.findAll();
	}

	@Override
	public PlantillaPlanTrabajo findPlantillaById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public PlantillaPlanTrabajo savePlantilla(PlantillaPlanTrabajo plantilla) {
		return repository.save(plantilla);
	}

	@Override
	public void deletePlantillaById(Long id) {
		repository.deleteById(id);
	}

}
