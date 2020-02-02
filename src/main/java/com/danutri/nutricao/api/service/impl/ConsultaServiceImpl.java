package com.danutri.nutricao.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danutri.nutricao.api.entidade.Consulta;
import com.danutri.nutricao.api.entidade.ConsultaAlteracoes;
import com.danutri.nutricao.api.repository.AlteracoesStatusRepository;
import com.danutri.nutricao.api.repository.ConsultaRepository;
import com.danutri.nutricao.api.service.ConsultaService;

@Service
public class ConsultaServiceImpl implements ConsultaService {

	@Autowired
	private ConsultaRepository consultaRepository;	
	@Autowired
	private AlteracoesStatusRepository alteracoesStatusRepository;
	
	@Override
	public Consulta createOrUpdate(Consulta consulta) {
		
		return this.consultaRepository.save(consulta);
	}

	@Override
	public Consulta findById(String id) {
		Optional<Consulta> optinalConsulta;
		optinalConsulta = this.consultaRepository.findById(id);
		Consulta consulta = optinalConsulta.get();
		return consulta;
	}

	@Override
	public void delete(String id) {
		this.consultaRepository.deleteById(id);
	}

	@Override
	public Page<Consulta> listConsulta(int page, int count) {
		Pageable pages = PageRequest.of(page, count);			
		return this.consultaRepository.findAll(pages);
	}

	@Override
	public ConsultaAlteracoes createChangeStatus(ConsultaAlteracoes consultaAlteracoes) {
		return this.alteracoesStatusRepository.save(consultaAlteracoes);
	}

	public Iterable<ConsultaAlteracoes> listChangeStatus(String ticketId) {
		return this.alteracoesStatusRepository.findByConsultaIdOrderByDataDesc(ticketId);
	}
	@Override
	public Page<Consulta> findByCurrentUser(int page, int count, String usuarioId) {
		Pageable pages = PageRequest.of(page, count);			
		return this.consultaRepository.findByUsuarioOrderByDateDesc(pages, usuarioId);
	}

	@Override
	public Page<Consulta> findByParameters(int page, int count, String title, String status) {
		Pageable pages = PageRequest.of(page, count);	
		return this.consultaRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingOrderByDateDesc(title, status, pages);
	}

	@Override
	public Page<Consulta> findByParametersCurrentUser(int page, int count, String title, String status, String usuarioId) {
		Pageable pages = PageRequest.of(page, count);	
		return this.consultaRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndUsuarioIdOrderByDateDesc(title, status, usuarioId, pages);
	}

	@Override
	public Page<Consulta> findByNumber(int page, int count, Integer number) {
		Pageable pages = PageRequest.of(page, count);	
		return this.consultaRepository.findByNumber(number, pages);
	}

	@Override
	public Iterable<Consulta> findAll() {
		return this.consultaRepository.findAll();
	}

	@Override
	public Page<Consulta> findByParametersAndAssignedUser(int page, int count, String title, String status,
			String nutricionistaId) {
		Pageable pages = PageRequest.of(page, count);	
		return this.consultaRepository.findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndNutricionistaIdOrderByDateDesc(title, status, nutricionistaId, pages);
	}

	@Override
	public Page<Consulta> findByHorIniAtenAndHorFinAten(int page, int count, String horIniAten, String horFinAten) {
		Pageable pages = PageRequest.of(page, count);	
		return this.consultaRepository.findByHorIniAtenIgnoreCaseContainingAndHorFinAtenIgnoreCaseContainingOrderByDateDesc(horIniAten, horFinAten , pages);
	}

}
