package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacto_cliente")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public @Data class ContactoCliente extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Exclude
	private Long id;

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

	@Override
	public String toString() {
		return "Contacto [nombre: " + nombre + ", telefono: " + telefono + ", correo: " + correo + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 771891741541469530L;

}
