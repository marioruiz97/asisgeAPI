package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etapa_plan_trabajo")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class EtapaPDT extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEtapaPDT;

	@ManyToOne(optional = false)
	@JsonBackReference
	@JoinColumn(name = "id_plan_de_trabajo", nullable = false)
	private PlanDeTrabajo planDeTrabajo;

	@NotBlank
	private String nombreEtapa;

	@NotNull
	private Date fechaInicio;

	@FutureOrPresent
	private Date fechaFin;

	@Override
	public String toString() {
		return "Etapa plan [id: " + idEtapaPDT + ", nombre etapa: " + nombreEtapa + ", fecha inicio: " + fechaInicio
				+ "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1019216997985431564L;

}
