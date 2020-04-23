package com.asisge.apirest.model.entity.actividades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.asisge.apirest.model.entity.proyectos.Proyecto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificacion_proyecto")
@NoArgsConstructor
@AllArgsConstructor
public @Data class Notificacion implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_proyecto")
	private Proyecto proyecto;

	@Enumerated(EnumType.STRING)
	private ColorNotificacion color;

	@NotBlank
	private String mensaje;

	@NotNull
	private Date fechaCreacion;

	@Positive
	@NotNull
	private int diasValido;

	public Notificacion(Proyecto proyecto, ColorNotificacion color, String mensaje, int diasValido) {
		this.proyecto = proyecto;
		this.color = color;
		this.mensaje = mensaje;
		this.diasValido = diasValido;
		this.fechaCreacion = new Date();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1354896239507865579L;
}
