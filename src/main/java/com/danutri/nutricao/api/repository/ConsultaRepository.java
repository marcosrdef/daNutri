package com.danutri.nutricao.api.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.danutri.nutricao.api.entidade.Consulta;

public interface ConsultaRepository extends MongoRepository<Consulta, String>{

	Page<Consulta> findByUserIdOrderByDesc(Pageable pages, String userId);
	
	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingOrderByDataDesc(
			String title, String status, Pageable pages);
	
	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingOrderAndUserIdByDataDesc(
			String title, String status, String userId, Pageable pages);
	
	Page<Consulta> findByNumber(Integer number, Pageable pages);
	
	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingOrderAndAssignedUserOrderByDataDesc(
			String title, String status, String assignedUser, Pageable pages);
	
	
}
