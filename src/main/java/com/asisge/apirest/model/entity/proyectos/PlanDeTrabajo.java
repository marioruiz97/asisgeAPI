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
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_de_trabajo")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class PlanDeTrabajo extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPlanDeTrabajo;

	@NotNull
	private Date fechaInicio;

	@FutureOrPresent
	private Date fechaFinEstimada;

	@FutureOrPresent
	private Date fechaFinReal;

	@PositiveOrZero
	private Integer horasMes;

	@NotBlank
	private String nombrePlan;

	@NotBlank
	private String objetivoPlan;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_proyecto", nullable = false)
	private Proyecto proyecto;

	@OneToMany(orphanRemoval = true, mappedBy = "planDeTrabajo")
	@JsonManagedReference
	private List<EtapaPDT> etapas;

	@OneToOne
	@JoinColumn(name = "etapa_actual")
	private EtapaPDT etapaActual;

	@Override
	public String toString() {
		return "Plan De Trabajo [id: " + idPlanDeTrabajo + ", fecha inicio: " + fechaInicio + ", Nombre del plan: "
				+ nombrePlan + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5018540033673416482L;
}
