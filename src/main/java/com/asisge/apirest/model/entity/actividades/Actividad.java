package com.asisge.apirest.model.entity.actividades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.asisge.apirest.model.entity.proyectos.EtapaPDT;
import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "actividad")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class Actividad extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idActividad;

	@NotBlank
	@Size(max = 100)
	@Column(name = "nombre_actividad")
	private String nombre;

	@ManyToMany(cascade = CascadeType.REMOVE)
	@JoinTable(name = "actividad_usuario", joinColumns = @JoinColumn(name = "id_actividad", nullable = false), 
		inverseJoinColumns = @JoinColumn(name = "id_usuario", nullable = false), 
		uniqueConstraints = @UniqueConstraint(columnNames = { "id_actividad", "id_usuario" }))
	private List<Usuario> responsables;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_etapa_actividad")
	private EtapaPDT etapa;

	@FutureOrPresent
	private Date fechaVencimiento;

	@NotNull
	@Positive
	@Column(name = "duracion_actividad")
	private Integer duracion;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "descripcion_actividad")
	private String descripcion;

	@Override
	public String toString() {
		return "Actividad [idActividad=" + idActividad + ", nombre=" + nombre + ", fechaVencimiento=" + fechaVencimiento
				+ ", duracion=" + duracion + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
