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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = { "identificacion", "correo" }))
@AllArgsConstructor
@NoArgsConstructor
public @Data class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotBlank
	@Size(max = 20)
	private String identificacion;

	@NotBlank
	@Size(max = 80)
	private String nombre;

	@NotBlank
	@Size(max = 40)
	private String apellido1;

	@Size(max = 40)
	private String apellido2;

	@Pattern(regexp = "(^$|[0-9]*)")
	@Size(max = 11)
	private String telefono;

	@Email(message = "El correo no tiene formato válido")
	@NotBlank
	@Size(max = 75)
	private String correo;

	@NotBlank
	private String contrasena;

	@NotNull
	private Boolean estado;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;

	@Override
	public String toString() {
		return "Usuario [Identificacion: " + identificacion + ", Nombre: " + nombre + " " + apellido1 + ", Correo: "
				+ correo + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1213182505799537662L;
}
