package com.danutri.nutricao.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danutri.nutricao.api.entidade.Consulta;
import com.danutri.nutricao.api.entidade.ConsultaAlteracoes;
import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.enums.Perfil;
import com.danutri.nutricao.api.enums.Status;
import com.danutri.nutricao.api.response.Response;
import com.danutri.nutricao.api.security.jwt.JwtTokenUtil;
import com.danutri.nutricao.api.security.model.Sumario;
import com.danutri.nutricao.api.service.ConsultaService;
import com.danutri.nutricao.api.service.UserService;

@RestController
@RequestMapping("api/consulta")
@CrossOrigin(origins = "*")
public class ConsultaController {

	private ConsultaService consultaService;
	private UserService userService;
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	public ConsultaController(ConsultaService consultaService, UserService userService, JwtTokenUtil jwtTokenUtil) {
		this.consultaService = consultaService;
		this.userService = userService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Consulta>> create(HttpServletRequest request, @RequestBody Consulta consulta,
			BindingResult result) {
		Response<Consulta> response = new Response<Consulta>();
		try {
			validateCreateConsulta(consulta, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			consulta.setNutricionista(userFromEmail(consulta.getNutricionista().getEmail()));
			consulta.setStatus(Status.Criado);
			consulta.setUsuario(userFromRequest(request));
			consulta.setDate(new Date());
			consulta.setNumber(generateNumber());
			Consulta consultaPersistida = (Consulta) consultaService.createOrUpdate(consulta);
			response.setData(consultaPersistida);

		} catch (Exception ex) {
			response.getErrors().add(ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('CLIENTE','NUTRICIONISTA')")
	public ResponseEntity<Response<Consulta>> update(HttpServletRequest request, @RequestBody Consulta consulta,
			BindingResult result) {
		Response<Consulta> response = new Response<Consulta>();
		try {
			Consulta consultaCurrent = validatePerfilandStatus(request, consulta, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			consultaCurrent.setDate(new Date());
			consultaCurrent.setStatus(consulta.getStatus());
			Consulta consultaPersistida = (Consulta) consultaService.createOrUpdate(consultaCurrent);
			ConsultaAlteracoes consultaAlteracoes = new ConsultaAlteracoes();
			consultaAlteracoes.setUsuario(userFromRequest(request));
			consultaAlteracoes.setData(new Date());
			consultaAlteracoes.setStatus(consulta.getStatus());
			consultaAlteracoes.setConsulta(consultaPersistida);
			consultaService.createChangeStatus(consultaAlteracoes);
			response.setData(consultaPersistida);

		} catch (Exception ex) {
			response.getErrors().add(ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('CLIENTE','NUTRICIONISTA')")
	public ResponseEntity<Response<Consulta>> findById(@PathVariable("id") String id) {
		Response<Consulta> response = new Response<Consulta>();
		Consulta consulta = consultaService.findById(id);
		if (consulta == null) {
			response.getErrors().add("consulta não encontrada id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		List<ConsultaAlteracoes> alteracoes = new ArrayList<ConsultaAlteracoes>();
		Iterable<ConsultaAlteracoes> consultaAlteracoesCurrent = consultaService
				.listConsultaAlteracoes(consulta.getId());
		for (Iterator<ConsultaAlteracoes> iterator = consultaAlteracoesCurrent.iterator(); iterator.hasNext();) {
			ConsultaAlteracoes consultaAlteracoes = iterator.next();
			consultaAlteracoes.setConsulta(null);
			alteracoes.add(consultaAlteracoes);
		}
		consulta.setAlteracoes(alteracoes);
		response.setData(consulta);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','NUTRICIONISTA')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Consulta consulta = consultaService.findById(id);
		if (consulta == null) {
			response.getErrors().add("consulta não encontrada id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		consultaService.delete(id);
		return ResponseEntity.ok(response);

	}

	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('CLIENTE','ADMIN','NUTRICIONISTA')")
	public ResponseEntity<Response<Page<Consulta>>> findAll(HttpServletRequest request, @PathVariable int page,
			@PathVariable int count) {

		Response<Page<Consulta>> response = new Response<Page<Consulta>>();
		Page<Consulta> consulta = null;
		Usuario userRequest = userFromRequest(request);
		switch (userRequest.getPerfil()) {
		case ROLE_ADMIN:
			consulta = consultaService.listConsulta(page, count);
			break;
		case ROLE_NUTRICIONISTA:
			consulta = consultaService.findByNutricionista(page, count, userRequest.getId());
			break;
		case ROLE_CLIENTE:
			consulta = consultaService.findByCurrentUser(page, count, userRequest.getId());
			break;
		}

		response.setData(consulta);
		return ResponseEntity.ok(response);
	}

	public ResponseEntity<Response<Sumario>> findChart() {
		Response<Sumario> response = new Response<Sumario>();
		Sumario sumario = new Sumario();
		int totalCriado = 0;
		int totalAgendado = 0;
		int totalCancelado = 0;
		int totalReagendado = 0;
		int totalmAtendimento = 0;
		int totalConcluido = 0;

		Iterable<Consulta> consultas = consultaService.findAll();

		if (consultas != null) {
			for (Iterator<Consulta> iterator = consultas.iterator(); iterator.hasNext();) {
				Consulta consulta = iterator.next();
				if (consulta.getStatus().equals(Status.Agendado)) {
					totalAgendado++;
				}
				if (consulta.getStatus().equals(Status.Cancelado)) {
					totalCancelado++;
				}
				if (consulta.getStatus().equals(Status.Criado)) {
					totalCriado++;
				}
				if (consulta.getStatus().equals(Status.Concluido)) {
					totalConcluido++;
				}
				if (consulta.getStatus().equals(Status.EmAtendimento)) {
					totalmAtendimento++;
				}
				if (consulta.getStatus().equals(Status.Reagendado)) {
					totalReagendado++;
				}
			}

		}

		sumario.setTotalAgendado(totalAgendado);
		sumario.setTotalCancelado(totalCancelado);
		sumario.setTotalConcluido(totalConcluido);
		sumario.setTotalCriado(totalCriado);
		sumario.setTotalmAtendimento(totalmAtendimento);
		sumario.setTotalReagendado(totalReagendado);

		response.setData(sumario);
		return ResponseEntity.ok(response);
	}

	private Consulta validatePerfilandStatus(HttpServletRequest request, Consulta consulta, BindingResult result) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		Usuario usuario = userService.findByEmail(email);
		Consulta consultaaux = consultaService.findById(consulta.getId());
		if (consulta.getId() == null) {
			result.addError(new ObjectError("Consulta", "Id não informado"));
			return null;
		}
		if (consulta.getTitle() == null) {
			result.addError(new ObjectError("Consulta", "Título não informado"));
			return null;
		}
		if ((Perfil.ROLE_CLIENTE).equals(usuario.getPerfil()) && !(Status.Cancelado).equals(consulta.getStatus())) {
			result.addError(new ObjectError("Consulta",
					"o Cliente não pode alterar o status da consulta, contate o administrator"));
			return null;
		}
		if (consultaaux == null) {
			result.addError(new ObjectError("Consulta", "Consulta não encontrada no sistema"));
			return null;
		}
		return consultaaux;
	}

	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(999999);
	}

	private Usuario userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}
	
	private Usuario userFromEmail(String email) {		
		return userService.findByEmail(email);
	}

	private void validateCreateConsulta(Consulta consulta, BindingResult result) {
		if (consulta.getTitle() == null) {
			result.addError(new ObjectError("Consulta", "título da consulta não informado"));
			return;
		}
		
		if (consulta.getNutricionista() == null || consulta.getNutricionista().getEmail() == null) {
			result.addError(new ObjectError("Consulta", "é necessário informar a(o) nutricionista"));
			return;
		}

	}

}
