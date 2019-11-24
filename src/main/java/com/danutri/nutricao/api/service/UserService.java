package com.danutri.nutricao.api.service;

import org.springframework.data.domain.Page;

import com.danutri.nutricao.api.entidade.Usuario;

public interface UserService {
 
	Usuario findByEmail(String email);
	Usuario createOrUpdate(Usuario usuario);
	Usuario findById(String id);
	void delete(String id);
	Page<Usuario> findAll(int page, int count);
}
