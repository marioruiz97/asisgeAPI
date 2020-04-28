package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.asisge.apirest.model.entity.proyectos.Proyecto;
import com.asisge.apirest.model.entity.terceros.MiembroProyecto;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IMiembroProyectoDao extends JpaRepository<MiembroProyecto, Long> {

	@Transactional(readOnly = true)
	@Query("SELECT mp.usuario FROM MiembroProyecto mp WHERE mp.proyecto.idProyecto = ?1 ORDER BY mp.usuario.id ASC")
	List<Usuario> findUsuariosProyecto(Long idProyecto);

	@Transactional(readOnly = true)
	@Query("SELECT mp.proyecto FROM MiembroProyecto mp WHERE mp.usuario.id = ?1")
	List<Proyecto> findProyectosUsuario(Long idUsuario);
	
	@Transactional(readOnly = true)
	@Query("SELECT mp.proyecto FROM MiembroProyecto mp WHERE mp.usuario.correo = ?1")
	List<Proyecto> findProyectosByEmail(String email);

	@Transactional(readOnly = true)
	@Query("SELECT u FROM Usuario u LEFT JOIN MiembroProyecto mp ON u.idUsuario = mp.usuario.idUsuario AND mp.proyecto.idProyecto = ?1 WHERE mp.usuario.idUsuario IS NULL")
	List<Usuario> findPosiblesMiembros(Long idProyecto);
	
	List<MiembroProyecto> findByProyecto(Proyecto proyecto);
	
	@Transactional(readOnly = true)
	@Query("SELECT count(mp) FROM MiembroProyecto mp WHERE mp.proyecto.idProyecto = ?1 AND mp.usuario.correo = ?2")
	int existsEmailInProyecto(Long idProyecto, String email);

	@Modifying
	@Transactional
	@Query("DELETE FROM MiembroProyecto mp WHERE mp.proyecto.idProyecto = ?1 AND mp.usuario.id = ?2 ")
	int deleteMiembro(Long idProyecto, Long idUsuario);

}
