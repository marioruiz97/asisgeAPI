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
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plantilla_plan_trabajo")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class PlantillaPlanTrabajo extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlantilla;

	@NotBlank
	@Size(max = 100)
	private String nombrePlantilla;

	@NotBlank
	@Size(max = 100)
	private String descripcionPlantilla;

	@NotNull
	@Positive
	private Integer duracion;

	@NotNull
	@Positive
	private Integer horasMes;

	@UniqueElements
	@OneToMany(orphanRemoval = true, targetEntity = PlantillaEtapa.class)
	@JoinColumn(name = "id_plantilla_plan", nullable = false)
	private List<@Valid PlantillaEtapa> etapas;

	@Override
	public String toString() {
		return "PlantillaPlanTrabajo\n [idPlantilla=" + idPlantilla + ", \nnombrePlantilla=" + nombrePlantilla + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 888815658874146579L;

}
