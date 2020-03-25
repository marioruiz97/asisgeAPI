package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asisge.apirest.model.entity.audit.Auditoria;

@Repository
public interface IAuditRepository extends JpaRepository<Auditoria, Long> {

}
