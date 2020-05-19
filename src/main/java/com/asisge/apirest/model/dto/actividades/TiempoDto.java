package com.asisge.apirest.model.dto.actividades;

import java.io.Serializable;
import java.util.List;

import com.asisge.apirest.model.entity.actividades.Actividad;
import com.asisge.apirest.model.entity.actividades.Seguimiento;
import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class TiempoDto implements Serializable {

	private Usuario usuario;
	private Actividad actividad;
	private List<Seguimiento> seguimientos;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
