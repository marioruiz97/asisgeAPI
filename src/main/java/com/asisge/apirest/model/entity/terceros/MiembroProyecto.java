package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.asisge.apirest.model.entity.proyectos.Proyecto;

import lombok.Data;

@Entity
@Table(name = "miembro_proyecto")
public @Data class MiembroProyecto implements Serializable {

	// TODO verificar si se requiere dto e implementarlo, 
	// TODO verificar si se requiere repository
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idMiembroProyecto;

	@ManyToOne(optional = false)
	private Usuario usuario;

	@ManyToOne(optional = false)
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
