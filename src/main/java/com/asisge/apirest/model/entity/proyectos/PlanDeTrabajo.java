package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "plan_de_trabajo")
public @Data class PlanDeTrabajo implements Serializable {

	// TODO implementar dto 
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlanDeTrabajo;

	@NotNull
	private Date fechaInicio;
	private Date fechaFinEstimada;
	private Date fechaFinReal;
	private Integer duracion;
	private Integer horasMes;

	@NotBlank
	private String objetivoPlan;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_proyecto", nullable = false)
	private Proyecto proyecto;

	@OneToMany(orphanRemoval = true, mappedBy = "planDeTrabajo")
	private List<EtapaPDT> etapas;

	@OneToOne
	@JoinColumn(name = "etapa_actual")
	private EtapaPDT etapaActual;

	@Override
	public String toString() {
		return "Plan De Trabajo [id: " + idPlanDeTrabajo + ", fecha inicio: " + fechaInicio + ", objetivo del plan: "
				+ objetivoPlan + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5018540033673416482L;
}
