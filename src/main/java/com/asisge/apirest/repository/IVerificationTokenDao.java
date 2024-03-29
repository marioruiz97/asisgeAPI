package com.asisge.apirest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.audit.VerificationToken;
import com.asisge.apirest.model.entity.terceros.Usuario;

public interface IVerificationTokenDao extends JpaRepository<VerificationToken, Long> {

	List<VerificationToken> findByUsuario(Usuario usuario);

	Optional<VerificationToken> findByToken(String token);
}
