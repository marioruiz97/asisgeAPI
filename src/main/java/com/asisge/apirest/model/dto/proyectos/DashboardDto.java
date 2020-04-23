package com.asisge.apirest.model.dto.proyectos;

import java.io.Serializable;
import java.util.List;

import com.asisge.apirest.model.entity.actividades.Notificacion;
import com.asisge.apirest.model.entity.proyectos.EstadoProyecto;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public @Data class DashboardDto implements Serializable {

	private Long idDashboard;
	private Cliente cliente;
	private List<MiembroProyecto> miembros;
	private Proyecto proyecto;
	private EstadoProyecto estadoActual;
	private List<Notificacion> notificaciones;
	private List<EstadoProyectoLineDto> lineaEstados;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
