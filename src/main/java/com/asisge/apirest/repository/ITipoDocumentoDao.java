package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asisge.apirest.model.entity.terceros.TipoDocumento;

@Repository
public interface ITipoDocumentoDao extends JpaRepository<TipoDocumento, Long> {

}
