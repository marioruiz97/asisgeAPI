package com.asisge.apirest.model.entity.proyectos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;

import lombok.Data;

@Entity
@Table(name = "proyecto")
public @Data class Proyecto implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProyecto;

	@NotBlank
	@Size(max = 50)
	private String nombreProyecto;

	@Lob
	private String descripcionGeneral;

	@FutureOrPresent
	@Nullable
	private Date fechaCierreProyecto;

	@ManyToOne(optional = false)
	private EstadoProyecto estadoProyecto;

	@OneToMany(mappedBy = "proyecto", orphanRemoval = true)
	private List<MiembroProyecto> miembrosProyecto;

	@ManyToOne(optional = false)
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
