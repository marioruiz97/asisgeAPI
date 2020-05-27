package com.asisge.apirest.model.entity.proyectos;

public enum TipoCierre {
	CIERRE_ETAPA("CIERRE_ETAPA"), CIERRE_PLAN("CIERRE_PLAN");

	private final String value;

	TipoCierre(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
