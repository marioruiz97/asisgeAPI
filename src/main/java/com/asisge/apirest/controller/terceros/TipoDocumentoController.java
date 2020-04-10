package com.asisge.apirest.controller.terceros;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asisge.apirest.controller.BaseController;
import com.asisge.apirest.model.entity.terceros.TipoDocumento;
import com.asisge.apirest.repository.ITipoDocumentoDao;
import com.asisge.apirest.config.paths.Paths.MaestrosPath;
import com.asisge.apirest.config.response.ApiResponse;
import com.asisge.apirest.config.utils.Messages;

@RestController
public class TipoDocumentoController extends BaseController {
	
	private static final String NOMBRE_TIPO = "nombreTipoDocumento";

	@Autowired
	private ITipoDocumentoDao repository;

	@GetMapping(MaestrosPath.TIPO_DOCUMENTOS)
	public ResponseEntity<ApiResponse> findAll() {
		List<TipoDocumento> tipos = repository.findAll(Sort.by(Direction.ASC, NOMBRE_TIPO));
		if (tipos.isEmpty()) {
			return respondNotFound(null);
		}
		return new ResponseEntity<>(buildOk(tipos), HttpStatus.OK);
	}

	@GetMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> findById(@PathVariable("idTipo") Long id) {
		TipoDocumento tipoDocumento = repository.findById(id).orElse(null);
		if (tipoDocumento == null) {
			return respondNotFound(id.toString());
		}
		return new ResponseEntity<>(buildOk(tipoDocumento), HttpStatus.OK);
	}

	@PostMapping(MaestrosPath.TIPO_DOCUMENTOS)
	public ResponseEntity<ApiResponse> create(@RequestBody ModelMap model) {
		TipoDocumento newTipo = new TipoDocumento();
		newTipo.setNombreTipoDocumento(model.get(NOMBRE_TIPO).toString());
		newTipo = repository.save(newTipo);
		String descripcion = String.format(RESULT_CREATED, newTipo.toString(), newTipo.getId());
		auditManager.saveAudit(newTipo.getCreatedBy(), ACTION_CREATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, newTipo, ""), HttpStatus.CREATED);
	}

	@PatchMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> update(@RequestBody ModelMap model, @PathVariable("idTipo") Long id) {
		TipoDocumento tipo = repository.findById(id).orElse(null);
		if (tipo == null) {
			return respondNotFound(id.toString());
		}
		tipo.setId(id);
		tipo.setNombreTipoDocumento(model.get(NOMBRE_TIPO).toString());
		tipo = repository.save(tipo);
		String descripcion = String.format(RESULT_UPDATED, tipo.toString(), tipo.getId());
		auditManager.saveAudit(tipo.getLastModifiedBy(), ACTION_UPDATE, descripcion);
		return new ResponseEntity<>(buildSuccess(descripcion, tipo, ""), HttpStatus.CREATED);
	}

	@DeleteMapping(MaestrosPath.TIPO_DOCUMENTO_ID)
	public ResponseEntity<ApiResponse> delete(@PathVariable("idTipo") Long id) {
		try {			 
			repository.deleteById(id);
			ApiResponse response = buildDeleted("tipo de documento", id.toString());
			String descripcion = response.getMessage();
			auditManager.saveAudit(ACTION_DELETE, descripcion);
			return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			String message = String.format(Messages.getString("message.error.delete.record"), "tipo de documento", id.toString());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, e);
		}
	}
}
