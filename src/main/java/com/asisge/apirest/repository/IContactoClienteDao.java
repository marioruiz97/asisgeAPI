package com.asisge.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asisge.apirest.model.entity.terceros.ContactoCliente;

public interface IContactoClienteDao extends JpaRepository<ContactoCliente, Long> {

}
