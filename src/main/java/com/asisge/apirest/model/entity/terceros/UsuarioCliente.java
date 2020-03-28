package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_cliente", uniqueConstraints = @UniqueConstraint(columnNames = {"id_usuario", "id_cliente"}))
@NoArgsConstructor
@AllArgsConstructor
public @Data class UsuarioCliente implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_cliente")
	private Cliente cliente;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
