package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import com.asisge.apirest.model.entity.audit.AuditModel;
import com.asisge.apirest.model.entity.terceros.Cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proyecto")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class Proyecto extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProyecto;

	@NotBlank
	@Size(max = 50)
	private String nombreProyecto;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String descripcionGeneral;

	@FutureOrPresent
	@Nullable
	private Date fechaCierreProyecto;

	@ManyToOne(optional = false)
	private EstadoProyecto estadoProyecto;	

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	@Override
	public String toString() {
		return "Proyecto [id: " + idProyecto + ", nombre: " + nombreProyecto + ", descripcion general: "
				+ descripcionGeneral + ", cliente: " + cliente.getIdentificacion() + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1309137622253215815L;

}
