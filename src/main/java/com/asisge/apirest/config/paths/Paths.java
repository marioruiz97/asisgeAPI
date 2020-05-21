package com.asisge.apirest.config.paths;

/**
 * 
 * Use the following Conventions when naming URI's (paths):
 * <ul>
 * <li>Use nouns to represent resources</li>
 * <li>Consistency is the key (use / to indicate hierarchy and never end an uri with /)</li>
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
	 * Rutas para: Autenticación - Recuperar clave
	 * 
	 * @author Mario Ruiz
	 */
	public static final class AuthPath {
		public static final String ME = "me";		
		public static final String CAMBIO_CONTRASENA = "cambio-contrasena/{usuario}";
		public static final String RECUPERAR = "cuenta/recuperar";
		public static final String CONFIRMAR = "cuenta/confirmar";

		private AuthPath() {
		}
	}

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
		public static final String CONTACTOS = CLIENTE_ID + "/contactos";
		public static final String CONTACTO_ID = "contactos/{id}";
		public static final String CAMBIO_ESTADO = "estado-usuario/{idUsuario}";
		public static final String ASESORES = "usuarios/clientes";
		public static final String CLIENTE_ASESOR = "clientes/usuarios/{idCliente}";

		private TercerosPath() {
		}
	}

	/**
	 * Rutas para maestros básicos: Tipos de documentos, estados de proyectos, etc.
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class MaestrosPath {
		public static final String TIPO_DOCUMENTOS = "tipo-documento";
		public static final String TIPO_DOCUMENTO_ID = "tipo-documento/{idTipo}";
		public static final String ESTADO_PROYECTO = "estado-proyecto";
		public static final String ESTADO_PROYECTO_ID = "estado-proyecto/{idEstado}";
		public static final String ESTADO_ACTIVIDAD = "estado-actividad";
		public static final String ESTADO_ACTIVIDAD_ID = "estado-actividad/{idEstado}";
		public static final String POSIBLES_ESTADOS = ESTADO_PROYECTO_ID + "/estados-siguientes";
		public static final String PLANTILLA = "plantilla";
		public static final String PLANTILLA_ID = "plantilla/{idPlantilla}";
		public static final String APROBACIONES_PLAN = "planes/{idPlan}/aprobaciones";

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
		// TODO al terminar proyecto, verificar estas rutas
		public static final String PROYECTOS = "proyectos";
		public static final String PROYECTO_ID = "proyectos/{idProyecto}";
		public static final String DASHBOARD = "dashboard/{id}";
		public static final String POSIBLES_MIEMBROS = "posibles-miembros/{id}";
		public static final String PROYECTO_MIEMBROS = "proyectos/{idProyecto}/miembros";
		public static final String MIEMBRO_PROYECTOS = "usuarios/{idUsuario}/mis-proyectos";
		public static final String PLANES_TRABAJO = "proyectos/{idProyecto}/planes";
		public static final String PLAN_TRABAJO_ID = "planes/{idPlan}";
		public static final String ETAPA_PLAN = "planes/{idPlan}/etapas";
		public static final String ETAPA_PLAN_ID = ETAPA_PLAN + "/{idEtapa}";
		public static final String ACTIVIDADES_PLAN = "planes/{idPlan}/actividades";
		public static final String ACTIVIDADES_PLAN_ID = "planes/{idPlan}/actividades/{idActividad}";
		public static final String ACTIVIDAD_ID = "actividades/{idActividad}";
		public static final String SEGUIMIENTOS = ACTIVIDAD_ID + "/seguimientos";
		public static final String SEGUIMIENTO_ID = SEGUIMIENTOS + "/{idSeguimiento}";
		public static final String TIEMPOS = "planes/{idPlan}/tiempos";
		
		private ProyectosPath() {
		}
	}

	/**
	 * Rutas para notificaciones
	 * 
	 * @author Mario Ruiz
	 *
	 */
	public static final class NotificacionesPath {
		public static final String NOTIFICACIONES = "notificaciones";
		public static final String NOTIFICACIONES_ID = "notificaciones/{id}";

		private NotificacionesPath() {
		}
	}

	private Paths() {
	}
}
