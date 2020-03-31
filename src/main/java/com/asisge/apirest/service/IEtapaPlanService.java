package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.dto.proyectos.EtapaDto;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;

public interface IEtapaPlanService {

	EtapaPDT saveEtapa(EtapaPDT etapa);

	List<EtapaPDT> saveAllEtapas(List<EtapaDto> dtoList);

	List<EtapaPDT> findAllEtapas();

	EtapaPDT findEtapaById(Long id);

	List<EtapaPDT> findEtapasByPlan(Long idPlan);

	void deleteEtapa(Long id);
	
	EtapaPDT buildEtapaEntity(EtapaDto dto);
}
