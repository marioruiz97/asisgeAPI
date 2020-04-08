package com.asisge.apirest.model.dto.terceros;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.asisge.apirest.model.entity.terceros.Role;

import lombok.Data;

public @Data class UsuarioDto implements Serializable {

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
	@Size(min = 6, max = 14)
	private String contrasena;
	private String matchContrasena;

	@NotNull
	private Boolean estado;

	@NotNull
	private Long tipoDocumento;
	
	private List<Role> roles;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

}