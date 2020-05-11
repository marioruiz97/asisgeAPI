package com.asisge.apirest.model.entity.actividades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_actividad")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class EstadoActividad extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEstado;

	@NotBlank
	private String nombreEstado;

	
	private String descripcion;

	private Boolean estadoInicial;
	private Boolean actividadNoAprobada;
	private Boolean actividadCompletada;

	@Override
	public String toString() {
		return "EstadoActividad [idEstado: " + idEstado + ", nombreEstado: " + nombreEstado + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1062289881960580246L;
}
