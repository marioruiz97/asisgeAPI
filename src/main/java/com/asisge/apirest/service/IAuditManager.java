package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.audit.Auditoria;

public interface IAuditManager {

	void saveAudit(String email, String accion, String descripcion);

	List<Auditoria> getAudits();
}
