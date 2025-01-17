package com.danutri.nutricao.api.service.impl;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.repository.UserRepository;
import com.danutri.nutricao.api.service.UserService;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public Usuario findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	@Override
	public Usuario createOrUpdate(Usuario usuario) {
		
		return userRepository.save(usuario);
	}

	@Override
	public Usuario findById(String id) {		
		return this.userRepository.findById(id).get();
	}

	@Override
	public void delete(String id) {
		this.userRepository.deleteById(id);
		
	}

	@Override
	public Page<Usuario> findAll(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.userRepository.findAll(pages);
	}

}
