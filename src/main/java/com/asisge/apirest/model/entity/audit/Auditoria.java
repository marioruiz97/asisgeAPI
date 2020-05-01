package com.asisge.apirest.model.entity.audit;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auditoria")
@NoArgsConstructor
public @Data class Auditoria implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAuditoria;
	private String emailUsuario;
	private Date fechaAccion;
	private String accionRealizada;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String descripcionAccion;


	/**
	 * 
	 */
	private static final long serialVersionUID = 1571649936205372L;

}