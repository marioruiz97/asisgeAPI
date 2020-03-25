package com.asisge.apirest.config.paths;

/**
 * 
 * Use the following Conventions when naming URI's (paths):
 * <ul>
 * <li>Use nouns to represent resources</li>
 * <li>Consistency is the key (use / to indicate hierarchy and never end an uri
 * with /)</li>
 * <li>Use Hyphens (-) to improve readability, do not use Underscores (_)</li>
 * <li>Use always lower case characters</li>
 * <li>Use Query component to filter collections Ex: route?axy=lol&zab=1</li>
 * <li>Do not use file extensions</li>
 * </ul>
 * 
 * @author Mario Ruiz
 */
public final class Paths {

	/**
	 * Rutas para: - Clientes - Usuarios
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class TercerosPath {
		public static final String USUARIO_ID = "usuarios/{idUsuario}";
		public static final String USUARIOS = "usuarios";
		public static final String CLIENTES = "clientes";
		public static final String CLIENTE_ID = "clientes/{idCliente}";

		private TercerosPath() {
		}
	}

	/**
	 * Rutas para: - Login (inicio de sesion) - Recovery (recuperar clave)
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class LoginPath {
		public static final String LOGIN = "Path.LOGIN";
		public static final String RECOVERY = "Path.RECOVERY";

		private LoginPath() {
		}
	}

	/**
	 * Rutas para maestros básicos: Tipos de documentos, estados de proyectos, etc.
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class MaestrosPath {
		// TODO crear las rutas para estos paths
		public static final String TIPO_DOCUMENTOS = "tipo-documento";
		public static final String TIPO_DOCUMENTO_ID = "tipo-documento/{idTipo}";
		public static final String ESTADO_PROYECTO = "estado-proyecto";
		public static final String ESTADO_PROYECTO_ID = "estado-proyecto/{idEstado}";

		private MaestrosPath() {
		}

	}

	/**
	 * Rutas para el core del sistema (gestion de proyectos): - proyectos - planes
	 * de trabajo - etapas de proyecto - miembros
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class ProyectosPath {
		// TODO crear las rutas para estos paths
		public static final String PROYECTOS = "proyectos";
		public static final String PROYECTO_ID = "proyectos/{idProyecto}";
		public static final String PROYECTO_MIEMBROS = "proyectos/{idProyecto}/miembros";
		public static final String PLANES_TRABAJO = "planes-trabajo";
		public static final String ETAPA_PLAN = "etapas-planes";
		public static final String ETAPA_PLAN_ID = "etapas-planes/{idEtapa}";

		private ProyectosPath() {
		}
	}

	private Paths() {
	}
}
