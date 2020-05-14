package com.asisge.apirest.model.entity.actividades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Type;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seguimiento_actividad")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class Seguimiento extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSeguimiento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_actividad_asociada")
	private Actividad actividadAsociada;
	

	@NotNull
	@Positive
	private Integer horasTrabajadas;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario_seguimiento")
	private Usuario usuarioSeguimiento;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@NotBlank
	private String observaciones;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@NotBlank
	private String descripcionLabor;

	@Override
	public String toString() {
		return "Seguimiento [idSeguimiento=" + idSeguimiento + ", fechaSeguimiento=" + getCreatedDate() + ", horasTrabajadas=" + horasTrabajadas + "]";
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7813630259155405968L;

}
