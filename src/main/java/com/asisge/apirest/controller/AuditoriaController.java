package com.asisge.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;
import com.asisge.apirest.model.entity.audit.Auditoria;
import com.asisge.apirest.repository.IVerificationTokenDao;
import com.asisge.apirest.service.IAuditManager;

@RestController
@RequestMapping("/audit")
public class AuditoriaController extends BaseController {

	@Autowired
	private IAuditManager managerService;

	@Autowired
	private IVerificationTokenDao tokenDao;

	@GetMapping
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse> getAllAudits() {
		List<Auditoria> auditorias = managerService.getAudits();
		if (auditorias.isEmpty())
			return respondNotFound(null);
		return new ResponseEntity<>(buildOk(auditorias), HttpStatus.OK);
	}

	@DeleteMapping("/delete-tokens")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ApiResponse> deleteTokens() {
		if (isAuthenticated()) {
			tokenDao.deleteAll();
			String accion = Messages.getString("message.action.delete-tokens");
			String descripcion = Messages.getString("message.result.token-flushed");
			auditManager.saveAudit(accion, descripcion);
			return new ResponseEntity<>(buildMessage(descripcion), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
}
