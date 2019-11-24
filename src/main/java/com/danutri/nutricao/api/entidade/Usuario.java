package com.danutri.nutricao.api.entidade;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.danutri.nutricao.api.enums.Perfil;

@Document
public class Usuario {

	@Id
	private String id;
	@Indexed(unique = true)
	@NotBlank(message = "Preencha o email")
	@Email(message = "Email inválido")
	private String email;
	@NotBlank(message = "Preencha a senha")
	@Size(min = 6)
	private String senha;
	@NotBlank(message = "Preencha o cpf")
	private String cpf;
	@NotBlank(message = "Preencha o nome")
	private String nome;
	@NotBlank(message = "Preencha o sobrenome")
	private String sobrenome;
	@NotBlank(message = "Preencha o rn")
	private String rn;
    private Perfil perfil;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public String getRn() {
		return rn;
	}
	public void setRn(String rn) {
		this.rn = rn;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
    
}
