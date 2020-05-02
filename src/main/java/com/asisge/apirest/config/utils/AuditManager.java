package com.asisge.apirest.config.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.asisge.apirest.model.entity.audit.Auditoria;
import com.asisge.apirest.repository.IAuditRepository;
import com.asisge.apirest.service.IAuditManager;

@Service
public class AuditManager implements IAuditManager {

	@Autowired
	private IAuditRepository repository;

	@Override
	public void saveAudit(String accion, String descripcion) {
		String email = Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated)
				.map(Authentication::getName).orElse("sistemas@asisge.com");
		saveAudit(email, accion, descripcion);
	}

	@Override
	public void saveAudit(String email, String accion, String descripcion) {
		Auditoria auditoria = new Auditoria();
		auditoria.setAccionRealizada(accion);
		auditoria.setEmailUsuario(email);
		auditoria.setFechaAccion(new Date());
		auditoria.setDescripcionAccion(descripcion);

		repository.save(auditoria);
	}

	@Override
	public List<Auditoria> getAudits() {
		List<Auditoria> auditorias;
		try {
			auditorias = repository.findAll(Sort.by(Direction.DESC, "fechaAccion"));
		} catch (Exception e) {
			auditorias = new ArrayList<>();
		}
		return auditorias;
	}

}
