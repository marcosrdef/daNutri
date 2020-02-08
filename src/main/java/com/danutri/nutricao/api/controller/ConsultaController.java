package com.danutri.nutricao.api.controller;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	@PreAuthorize("hasAnyRole('CLIENTE','ROLE_NUTRICIONISTA')")
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
			
		}catch(Exception ex) {
			response.getErrors().add(ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private Consulta validatePerfilandStatus(HttpServletRequest request, Consulta consulta, BindingResult result) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		Usuario usuario = userService.findByEmail(email);
		Consulta consultaaux = consultaService.findById(consulta.getId());
		if (consulta.getId() == null) {
			result.addError(new ObjectError("Consulta","Id não informado"));
			return null;
		}
		if (consulta.getTitle() == null) {
			result.addError(new ObjectError("Consulta","Título não informado"));
			return null;
		}
		if ((Perfil.ROLE_CLIENTE).equals(usuario.getPerfil()) && !(Status.Cancelado).equals(consulta.getStatus())) {
			result.addError(new ObjectError("Consulta","o Cliente não pode alterar o status da consulta, contate o administrator"));
			return null;
		}
		if (consultaaux == null) {
			result.addError(new ObjectError("Consulta","Consulta não encontrada no sistema"));
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

	private void validateCreateConsulta(Consulta consulta, BindingResult result) {
		if (consulta.getTitle() == null) {
			result.addError(new ObjectError("Consulta", "título da consulta não informado"));
			return;
		}

	}

}
