package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.proyectos.Proyecto;

public interface IProyectoDao extends JpaRepository<Proyecto, Long> {

}
