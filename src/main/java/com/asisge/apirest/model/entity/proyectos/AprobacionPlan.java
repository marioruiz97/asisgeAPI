package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aprobacion_plan")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class AprobacionPlan extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "id_plan_trabajo", unique = true)
	private PlanDeTrabajo plan;

	private Boolean avalCliente;

	private String observaciones;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4061145573593464464L;

}
