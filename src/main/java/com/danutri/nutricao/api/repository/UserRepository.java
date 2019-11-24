package com.danutri.nutricao.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.danutri.nutricao.api.entidade.Usuario;

public interface UserRepository extends MongoRepository<Usuario, String> {

	Usuario findByEmail(String email);
}
