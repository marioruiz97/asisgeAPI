package com.asisge.apirest.model.entity.actividades;

public enum ColorNotificacion {
	PRIMARY("primary"),
	WARN("warn"),
	INFO("info"),
	SUCCESS("success");
	
	private final String value;

	ColorNotificacion(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
