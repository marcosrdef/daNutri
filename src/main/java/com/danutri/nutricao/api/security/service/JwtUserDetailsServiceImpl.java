package com.danutri.nutricao.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.security.jwt.JwtUserFactory;
import com.danutri.nutricao.api.service.UserService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = userService.findByEmail(email);
		if (usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuário não encontrado com o email '%s.'", email));
		}
		else {
			return JwtUserFactory.create(usuario);
		}
	}
	
	
}
