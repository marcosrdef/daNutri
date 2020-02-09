package com.danutri.nutricao.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.danutri.nutricao.api.entidade.Consulta;

public interface ConsultaRepository extends MongoRepository<Consulta, String> {

	Page<Consulta> findByUsuarioIdOrderByDateDesc(Pageable pages, String usuarioId);

	Page<Consulta> findByNutricionistaIdOrderByDateDesc(Pageable pages, String nutricionistaId);

	Consulta findByNutricionistaIdAndHorIniAtenAndDataConsultaOrderByDateDesc(String nutricionistaId, String horIniAten,
			String dataConsulta);

	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingOrderByDateDesc(String title,
			String status, Pageable pages);

	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndUsuarioIdOrderByDateDesc(String title,
			String status, String usuarioId, Pageable pages);

	Page<Consulta> findByHorIniAtenIgnoreCaseContainingAndHorFinAtenIgnoreCaseContainingOrderByDateDesc(
			String horIniAten, String horFinAten, Pageable pages);

	Page<Consulta> findByNumber(Integer number, Pageable pages);

	Page<Consulta> findByTitleIgnoreCaseContainingAndStatusIgnoreCaseContainingAndNutricionistaIdOrderByDateDesc(
			String title, String status, String nutricionistaId, Pageable pages);

}
