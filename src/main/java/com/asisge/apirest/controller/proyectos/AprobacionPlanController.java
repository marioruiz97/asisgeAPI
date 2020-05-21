package com.asisge.apirest.controller.proyectos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.repository.IAprobacionDao;

@RestController
@RequestMapping(value = MaestrosPath.APROBACIONES_PLAN)
public class AprobacionPlanController extends BaseController {

	@Autowired
	private IAprobacionDao repository;

	@GetMapping
	public ResponseEntity<ApiResponse> getAprobacion() {
		return null;
	}

	@PostMapping
	public ResponseEntity<ApiResponse> saveAprobacion() {
		return null;
	}
	
}
