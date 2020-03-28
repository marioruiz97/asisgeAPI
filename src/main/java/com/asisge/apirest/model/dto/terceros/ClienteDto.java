package com.asisge.apirest.model.dto.terceros;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.Data;

public @Data class ClienteDto implements Serializable {

	@NotBlank
	@Size(max = 20)
	private String identificacion;

	@NotBlank
	private String nombreComercial;

	@NotBlank
	private String razonSocial;

	@NotNull
	private Long tipoDocumento;

	@UniqueElements
	@Valid
	private List<ContactoDto> contactos;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
}
