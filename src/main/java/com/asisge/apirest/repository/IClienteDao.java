package com.asisge.apirest.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.asisge.apirest.model.entity.terceros.Cliente;
import com.asisge.apirest.model.entity.terceros.ContactoCliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

	@Query("SELECT c.contactos FROM Cliente c WHERE c.idCliente = ?1")
	List<ContactoCliente> findContactosById(Long idCliente, Sort sort);
	
}
