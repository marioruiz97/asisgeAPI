package com.asisge.apirest.service;

import java.util.List;

import com.asisge.apirest.model.entity.proyectos.Cierre;

public interface ICierreService {

	Cierre saveCierre(Cierre cierre);

	List<Cierre> findAll();

}
