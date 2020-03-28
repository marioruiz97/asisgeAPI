package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente", uniqueConstraints = @UniqueConstraint(columnNames = "identificacion"))
@NoArgsConstructor
@AllArgsConstructor
public @Data class Cliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCliente;

	@NotBlank
	@Size(max = 20)
	private String identificacion;

	@NotBlank
	private String nombreComercial;

	@NotBlank
	private String razonSocial;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	private TipoDocumento tipoDocumento;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "cliente_asociado")
	@UniqueElements
	private List<ContactoCliente> contactos;

	public Cliente(Long id) {
		this.idCliente = id;
	}

	@Override
	public String toString() {
		return "Cliente [identificacion: " + identificacion + ", nombre comercial: " + nombreComercial + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3122561620795486262L;
}
