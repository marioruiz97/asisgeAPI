package com.asisge.apirest.model.entity.plantillas;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plantilla_etapa")
@NoArgsConstructor
@AllArgsConstructor
public @Data class PlantillaEtapa implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlantillaEtapa;

	@NotBlank
	private String nombreEtapa;

	@NotNull
	@PositiveOrZero
	private Integer duracion;

	@UniqueElements
	@OneToMany(orphanRemoval = true, targetEntity = PlantillaActividad.class)
	@JoinColumn(name = "id_etapa_actvidad", nullable = false)
	private List<@Valid PlantillaActividad> actividades;

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8887363594441332725L;

}
