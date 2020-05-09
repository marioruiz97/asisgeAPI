package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.List;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class EtapaBoard implements Serializable {

	private EtapaPDT etapa;
	private List<Actividad> actividades;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
