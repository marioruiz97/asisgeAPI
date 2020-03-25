package com.asisge.apirest.controller.terceros;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.repository.ITipoDocumentoDao;
import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;

@RestController
@RequestMapping("/maestros/")
public class TipoDocumentoController extends BaseController {

	@Autowired
	private ITipoDocumentoDao repository;

	@GetMapping(MaestrosPath.TIPO_DOCUMENTOS)
	public ResponseEntity<ApiResponse> findAll() {
		List<TipoDocumento> tipos = repository.findAll(Sort.by(Direction.ASC, "nombreTipoDocumento"));
		return new ResponseEntity<>(buildSuccess("", tipos, ""), HttpStatus.OK);
	}

	// forma real : findById(id).orElse(null);
	@GetMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable("idTipo") Long id) {
		ApiResponse response;
		try {
			TipoDocumento tipoDocumento = repository.getOne(id);
			response = buildSuccess("", tipoDocumento, "");
		} catch (EntityNotFoundException e) {
			response = buildSuccess("", e, "");
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(MaestrosPath.TIPO_DOCUMENTOS)
	public ResponseEntity<ApiResponse> create(@RequestBody ModelMap model) {
		TipoDocumento newTipo = new TipoDocumento();
		newTipo.setNombreTipoDocumento(model.get("nombre").toString());
		newTipo = repository.save(newTipo);
		String descripcion = String.format(RESULT_CREATED, newTipo.toString(), newTipo.getId());
		auditManager.saveAudit(getEmail(model), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newTipo, ""), HttpStatus.CREATED);
	}

	@PatchMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> update() {
		return null;
	}

	@DeleteMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> delete() {
		return null;
	}
}
