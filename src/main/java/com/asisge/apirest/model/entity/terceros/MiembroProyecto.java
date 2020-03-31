package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "miembro_proyecto", uniqueConstraints = @UniqueConstraint(columnNames = { "id_proyecto", "id_usuario" }))
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class MiembroProyecto extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idMiembroProyecto;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_proyecto")
	@JsonManagedReference
	private Proyecto proyecto;

	@NotBlank
	private String rolProyecto;

	@Override
	public String toString() {
		return "MiembroProyecto [idMiembroProyecto: " + idMiembroProyecto + ", usuario: " + usuario + ", proyecto: "
				+ proyecto + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6486632708093280227L;

}
