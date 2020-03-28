package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_proyecto")
@NoArgsConstructor
@AllArgsConstructor
public @Data class EstadoProyecto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String nombreEstado;
	
	private String descripcion;
	
	private Long idEstadoAnterior;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5193564704319983604L;
}
