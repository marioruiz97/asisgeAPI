package com.asisge.apirest.service;

import java.io.FileNotFoundException;

import net.sf.jasperreports.engine.JRException;

public interface IReporteService {

	void generarReporteEtapas(Long idProyecto) throws FileNotFoundException, JRException;

}
