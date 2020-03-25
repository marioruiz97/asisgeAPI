package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tipo_documento")
public class TipoDocumento implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre de tipo de documento es obligatorio")
	@Size(max = 10)
	private String nombreTipoDocumento;

	/**
	 * metodos
	 */
	public TipoDocumento() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nombreTipoDocumento);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TipoDocumento other = (TipoDocumento) obj;
		return Objects.equals(id, other.id) && Objects.equals(nombreTipoDocumento, other.nombreTipoDocumento);
	}

	@Override
	public String toString() {
		return "TipoDocumento\n [id=" + id + ", \nnombreTipoDocumento=" + nombreTipoDocumento + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreTipoDocumento() {
		return nombreTipoDocumento;
	}

	public void setNombreTipoDocumento(String nombreTipoDocumento) {
		this.nombreTipoDocumento = nombreTipoDocumento;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5751992602034044801L;

}
