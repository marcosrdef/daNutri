package com.danutri.nutricao.api.entidade;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.danutri.nutricao.api.enums.Status;
@Document
public class Consulta {
	@Id
	private String id;
	@DBRef(lazy=true)
	private Usuario usuario;
	private Date date;
	private String title;
	private String mensagem;
	private String email;
	@DBRef
	private Usuario nutricionista;
	private Status status;
	@Transient
	private List<ConsultaAlteracoes> alteracoes;
	public List<ConsultaAlteracoes> getAlteracoes() {
		return alteracoes;
	}
	public void setAlteracoes(List<ConsultaAlteracoes> alteracoes) {
		this.alteracoes = alteracoes;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Usuario getNutricionista() {
		return nutricionista;
	}
	public void setNutricionista(Usuario nutricionista) {
		this.nutricionista = nutricionista;
	}
	
}
