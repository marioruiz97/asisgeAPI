package com.asisge.apirest.model.dto.terceros;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

public @Data class ContactoDto implements Serializable {

	@NotBlank
	@Size(max = 80)
	private String nombre;

	@Size(max = 11)
	@Pattern(regexp = "(^$|[0-9]*)")
	private String telefono;

	@Email
	@NotBlank
	@Size(max = 75)
	private String correo;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
