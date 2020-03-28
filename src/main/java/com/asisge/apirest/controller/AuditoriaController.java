package com.asisge.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.model.entity.audit.Auditoria;
import com.asisge.apirest.service.IAuditManager;

@RestController
@RequestMapping("/audit")
public class AuditoriaController extends BaseController {

	@Autowired
	private IAuditManager managerService;

	@GetMapping
	public ResponseEntity<ApiResponse> getAllAudits() {
		ApiResponse response;
		try {
			List<Auditoria> auditorias = managerService.getAudits();
			if (auditorias.isEmpty()) {
				return respondNotFound(null);
			} else {
				response = buildSuccess(RESULT_SUCCESS, auditorias, "Consulta de auditoria");
			}
		} catch (Exception e) {
			response = buildFail(e.getLocalizedMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
