package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.plantillas.PlantillaPlanTrabajo;

public interface IPlantillaService {

	List<PlantillaPlanTrabajo> findPlantillas();

	PlantillaPlanTrabajo findPlantillaById(Long id);

	PlantillaPlanTrabajo savePlantilla(PlantillaPlanTrabajo plantilla);

	void deletePlantillaById(Long id);

}
