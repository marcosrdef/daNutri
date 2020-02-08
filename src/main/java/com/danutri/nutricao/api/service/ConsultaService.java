package com.danutri.nutricao.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.danutri.nutricao.api.entidade.Consulta;
import com.danutri.nutricao.api.entidade.ConsultaAlteracoes;

@Component
public interface ConsultaService {

	Consulta createOrUpdate(Consulta consulta);
	Consulta findById(String id);
	void delete (String id);
	Page<Consulta> listConsulta(int page, int count);
	ConsultaAlteracoes createChangeStatus(ConsultaAlteracoes consultaAlteracoes);
	Iterable<ConsultaAlteracoes> listConsultaAlteracoes(String consultaId);
	Page<Consulta> findByCurrentUser(int page, int count, String userId);
	Page<Consulta> findByNutricionista(int page, int count, String nutricionistaId);
	Page<Consulta> findByParameters(int page, int count, String title, String status);
	Page<Consulta> findByParametersCurrentUser(int page, int count, String Title, String status, String userId);
	Page<Consulta> findByNumber(int page, int count, Integer number);
	Page<Consulta> findByHorIniAtenAndHorFinAten(int page, int count, String horIniAten, String horFinAten);
	Iterable<Consulta> findAll();
	public Page<Consulta> findByParametersAndAssignedUser(int page, int count, String title, String status, String assignedUser);
}
