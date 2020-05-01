package com.asisge.apirest.model.entity.audit;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "auditoria")
public class Auditoria implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAuditoria;
	private String emailUsuario;
	private Date fechaAccion;
	private String accionRealizada;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String descripcionAccion;

	
	public Auditoria() {
		// empty constructor for POJO conventions
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAuditoria);
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
		Auditoria other = (Auditoria) obj;
		return Objects.equals(idAuditoria, other.idAuditoria);
	}

	@Override
	public String toString() {
		return "Auditoria\n [idAuditoria=" + idAuditoria + ", \nemailUsuario=" + emailUsuario + ", \nfechaAccion="
				+ fechaAccion + ", \naccionRealizada=" + accionRealizada + ", \ndescripcionAccion=" + descripcionAccion
				+ "]";
	}

	public Long getIdAuditoria() {
		return idAuditoria;
	}

	public void setIdAuditoria(Long idAuditoria) {
		this.idAuditoria = idAuditoria;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Date getFechaAccion() {
		return fechaAccion;
	}

	public void setFechaAccion(Date fechaAccion) {
		this.fechaAccion = fechaAccion;
	}

	public String getAccionRealizada() {
		return accionRealizada;
	}

	public void setAccionRealizada(String accionRealizada) {
		this.accionRealizada = accionRealizada;
	}

	public String getDescripcionAccion() {
		return descripcionAccion;
	}

	public void setDescripcionAccion(String descripcionAccion) {
		this.descripcionAccion = descripcionAccion;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1571649936205372L;

}