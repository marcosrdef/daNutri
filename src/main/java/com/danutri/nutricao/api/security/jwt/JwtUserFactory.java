package com.danutri.nutricao.api.security.jwt;

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.enums.Perfil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public class JwtUserFactory {

	private JwtUserFactory() {
		
	}
	
	public static JwtUser create(Usuario usuario) {
		return new JwtUser(
				usuario.getId(),
				usuario.getEmail(),
				usuario.getSenha(),
				mapToGrantedAuthorities(usuario.getPerfil()));
	}
	
	private static List<GrantedAuthority> mapToGrantedAuthorities(Perfil perfil) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(perfil.toString()));
		return authorities;
	}
}
