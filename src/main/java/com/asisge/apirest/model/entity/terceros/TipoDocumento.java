package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "tipo_documento", uniqueConstraints = @UniqueConstraint(columnNames = "nombreTipoDocumento"))
public @Data class TipoDocumento implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 50)
	private String nombreTipoDocumento;

	@Override
	public String toString() {
		return "Tipo de Documento " + nombreTipoDocumento;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5751992602034044801L;

}
