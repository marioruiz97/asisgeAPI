package com.asisge.apirest.model.entity.terceros;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.asisge.apirest.model.entity.audit.AuditModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "perfil")
@EqualsAndHashCode(callSuper = false)
public @Data class Perfil extends AuditModel<String> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombrePerfil;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5979359890472846003L;
}
