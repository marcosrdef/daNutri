package com.danutri.nutricao.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.enums.Perfil;

public interface UserRepository extends MongoRepository<Usuario, String> {

	Usuario findByEmail(String email);

	Page<Usuario> findByPerfilIgnoreCaseContaining(Perfil perfil, Pageable pages);
}
