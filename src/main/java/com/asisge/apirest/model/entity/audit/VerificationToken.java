package com.asisge.apirest.model.entity.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.asisge.apirest.model.entity.terceros.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;

	private String token;

	@OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "usuario_id")
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	
	public VerificationToken(Usuario user) {
		this.usuario = user;
		this.createdDate = new Date();
		this.token = UUID.randomUUID().toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
