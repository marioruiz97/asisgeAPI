package com.asisge.apirest.model.dto.actividades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.Data;

public @Data class ActividadDto implements Serializable {

	@NotBlank
	@Size(max = 100)
	private String nombre;

	@UniqueElements
	private List<Long> responsables;

	@NotNull
	private Long etapa;
	
	@NotNull
	private Long estadoActividad;

	
	@NotNull
	private Date fechaVencimiento;

	@NotNull
	@Positive
	private Integer duracion;

	@NotBlank
	private String descripcion;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
