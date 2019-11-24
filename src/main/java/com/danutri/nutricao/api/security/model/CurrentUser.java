package com.danutri.nutricao.api.security.model;

import com.danutri.nutricao.api.entidade.Usuario;

public class CurrentUser {

	private String Token;
	private Usuario usuario;
	
	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CurrentUser(String token, Usuario usuario) {
		this.setUsuario(usuario);
		this.setToken(token);
	}
}
