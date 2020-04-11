package com.asisge.apirest.model.dto.terceros;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CuentaDto implements Serializable {

	@NotNull
	private Long idUsuario;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
