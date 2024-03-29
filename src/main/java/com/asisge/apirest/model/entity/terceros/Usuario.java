package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "usuario", uniqueConstraints = @UniqueConstraint(columnNames = { "identificacion", "correo" }))
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class Usuario extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long idUsuario;

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
	@Column(nullable = false, length = 75, unique = true)
	private String correo;

	@NotBlank
	private String contrasena;

	@NotNull
	private Boolean estado;
	
	@NotNull 
	private Boolean verificado;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(joinColumns = @JoinColumn(name = "usuario_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"),
	uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "role_id"}) })
	private List<Role> roles;

	
	
	public Usuario(Long id) {
		this.idUsuario = id;
	}
	
	

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
