package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.EtapaPDT;

public interface IEtapaPlanDao extends JpaRepository<EtapaPDT, Long> {

}
