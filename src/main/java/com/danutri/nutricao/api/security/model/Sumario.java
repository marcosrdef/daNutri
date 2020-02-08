package com.danutri.nutricao.api.security.model;

import java.io.Serializable;

public class Sumario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalCriado;
	private Integer totalAgendado;
	private Integer totalCancelado;
	private Integer totalReagendado;
	private Integer totalmAtendimento;
	private Integer totalConcluido;

	public Integer getTotalCriado() {
		return totalCriado;
	}

	public void setTotalCriado(Integer totalCriado) {
		this.totalCriado = totalCriado;
	}

	public Integer getTotalAgendado() {
		return totalAgendado;
	}

	public void setTotalAgendado(Integer totalAgendado) {
		this.totalAgendado = totalAgendado;
	}

	public Integer getTotalCancelado() {
		return totalCancelado;
	}

	public void setTotalCancelado(Integer totalCancelado) {
		this.totalCancelado = totalCancelado;
	}

	public Integer getTotalReagendado() {
		return totalReagendado;
	}

	public void setTotalReagendado(Integer totalReagendado) {
		this.totalReagendado = totalReagendado;
	}

	public Integer getTotalmAtendimento() {
		return totalmAtendimento;
	}

	public void setTotalmAtendimento(Integer totalmAtendimento) {
		this.totalmAtendimento = totalmAtendimento;
	}

	public Integer getTotalConcluido() {
		return totalConcluido;
	}

	public void setTotalConcluido(Integer totalConcluido) {
		this.totalConcluido = totalConcluido;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
