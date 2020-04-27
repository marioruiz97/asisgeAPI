package com.asisge.apirest.model.entity.actividades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificacion_usuario", uniqueConstraints = @UniqueConstraint(columnNames = { "id_notificacion", "id_usuario" }))
@NoArgsConstructor
@AllArgsConstructor
public @Data class NotificacionUsuario implements Serializable {	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long idNotificacionUsuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_usuario")	
	private Usuario usuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_notificacion")	
	private Notificacion notificacion;

	@NotNull
	private Boolean visto;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6912576870448904579L;
}
