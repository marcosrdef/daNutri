package com.danutri.nutricao.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.danutri.nutricao.api.entidade.ConsultaAlteracoes;

public interface AlteracoesStatusRepository extends MongoRepository<ConsultaAlteracoes, String> {
	Iterable<ConsultaAlteracoes> findByConsultaIdOrderByDataDesc(String consultaId);
}
