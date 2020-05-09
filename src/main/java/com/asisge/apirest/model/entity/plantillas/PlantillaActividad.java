package com.asisge.apirest.model.entity.plantillas;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plantilla_actividad")
@NoArgsConstructor
@AllArgsConstructor
public @Data class PlantillaActividad implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idActividad;

	@NotBlank
	private String nombre;

	@NotNull
	@PositiveOrZero
	private Integer duracion;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7700339222119259434L;

}
