package com.asisge.apirest.config.auth;

public class JwtConfig {
	// TODO separar estas claves en un archivo aparte para no dejar las llaves expuestas

	public static final String LLAVE_SECRETA = "-----BEGIN RSA PRIVATE KEY-----\r\n"
			+ "MIIEowIBAAKCAQEAtRrrD2fGygDoEoSmkriisjshFpXDEIA6JRILStp3maSKvw64\r\n"
			+ "pcK5xHSF+4YQ1FoFG3tV7MC+HcOjog62/38aoPZg8qQ+Fk8eYTuYur+4uJwogn7P\r\n"
			+ "zYjBZ4NOkAL+6AcSJuRfZcDJxsi7zKMN29KEHCMutFazVP7dH+5b2luXCiJxejEG\r\n"
			+ "8hT3VGOi7uXoyYyvuN+PjY/crlmEik4SAq3ryufdsMvUkelYg321/9gac41DjEoX\r\n"
			+ "Hk5KtUJ0wuqlMPm4qnITyUUaejlQjwWlO9JTH/KtXScg+aq4FrO/GcI8h6Gbe2Cd\r\n"
			+ "vAb5LWPyMtaK0/P2i2Au5jCnN4zeLHqxU8hwkQIDAQABAoIBAEPUmymf8sKB6GnX\r\n"
			+ "KRRW9wjOQKxW10J85EoyIeydOqJfEGBSwTHEZxN/mRcFtZPzS2ppnVsZLZu0RLtT\r\n"
			+ "y5KSJoUXAmSew2pTo8tDKQb4+9SZGEsX9/jBOb7tXSVvGc4Jp/DNJDXWKTy8vin9\r\n"
			+ "ABnPFQ/81WdR+cTh2Cw3eEumdkGi15WoVA9wGI8KfdJS4IultZEUbtpLHHZcN0yP\r\n"
			+ "xHjdDZrRw4ZEuqsYpj9htIPDZuJN7HHu8NnoCvLesO0wQBFsjCPqsAl3nAMipaMa\r\n"
			+ "WVrsrOEzJbIThr4wulube58IibiAtQTBfxRlJQsMZraQgutnykCtH1x3uaI6DXTm\r\n"
			+ "lsH1wUkCgYEA2MnwkG3R0W8vwyVzLsMRJg7Ri3b1f20/1Po8krpSR8+0uEI2gXRr\r\n"
			+ "ELhoIpyfajVtYWVxcUJXeoaxYnxVjsR2brXGiYWPHefR4o1jmkVkXeLwLH/0yGPE\r\n"
			+ "bTRZn6ggsuWCo4yJRjWFqEiwxLCl86Hi80R12t/SPkkb1+v/nd+/aDcCgYEA1dyz\r\n"
			+ "61llVevYe5f2u6BAPpB7W8VmawF5U7FJCIw8yRci5MT0+SyEy5QvNleP7x04x+8I\r\n"
			+ "2ClsoriX1O5mZHsbirJUmeLYqMuSM/rVScFSnTcrReIrTt5I/ZlOYyrAS+a4mTgC\r\n"
			+ "Kq4dGGfdphVLS5VTePUdGR+4xOX54d078KJbeXcCgYEAiURsr3bqCRvG+WDIKw7W\r\n"
			+ "1+tAIXfEgNgTN+7u1dVmbhQFkTpTuuqVNHQfc/MKObz0slwCVb21GF8bCIpDvm+F\r\n"
			+ "sholSUSpVz1HN5VhQPi5UX11qCepPg11/dPM//v8IGqUqqw865jONb05OIHFwpt5\r\n"
			+ "0aFCosFgJZIzMCg9paBcWUcCgYAPgBWSQ0Jy/csM+jCp3K2LW1F1j1MjAlJW3Iyc\r\n"
			+ "j/1/3atrxVK0FKPzVWQunh4SiJ2q8ApGEIqNKK/ogPbu5XVTkiVLmgN2CHsqRU3c\r\n"
			+ "MKTaPDtor0HQT5AewVPLIDgbyDcs88BEc6YsNKr99KEmc0Y/iBR507DCpKB93FkR\r\n"
			+ "0dKrdQKBgBNR8tYl0BUVQlp8yaotb+RddiAnfEg5ySlA3bhNJVYhpfiPl/wKMzSK\r\n"
			+ "dlJMOY0/1sfa6QEozQTfCnptDQmaDojLuL1bIhicfkIOv4e8vro6ZIob9PXhSREK\r\n"
			+ "4D4Z2RXbbwwDSYjR89BwreYtVqLM+jbiPVWwU9sYqW7wdZCj1MGI\r\n" 
			+ "-----END RSA PRIVATE KEY-----";

	public static final String LLAVE_PUBLICA = "-----BEGIN PUBLIC KEY-----\r\n"
			+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtRrrD2fGygDoEoSmkrii\r\n"
			+ "sjshFpXDEIA6JRILStp3maSKvw64pcK5xHSF+4YQ1FoFG3tV7MC+HcOjog62/38a\r\n"
			+ "oPZg8qQ+Fk8eYTuYur+4uJwogn7PzYjBZ4NOkAL+6AcSJuRfZcDJxsi7zKMN29KE\r\n"
			+ "HCMutFazVP7dH+5b2luXCiJxejEG8hT3VGOi7uXoyYyvuN+PjY/crlmEik4SAq3r\r\n"
			+ "yufdsMvUkelYg321/9gac41DjEoXHk5KtUJ0wuqlMPm4qnITyUUaejlQjwWlO9JT\r\n"
			+ "H/KtXScg+aq4FrO/GcI8h6Gbe2CdvAb5LWPyMtaK0/P2i2Au5jCnN4zeLHqxU8hw\r\n" 
			+ "kQIDAQAB\r\n"
			+ "-----END PUBLIC KEY-----";

	private JwtConfig() {
	}
}
