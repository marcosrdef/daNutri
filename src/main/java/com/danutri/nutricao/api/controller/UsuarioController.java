package com.danutri.nutricao.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.danutri.nutricao.api.entidade.Usuario;
import com.danutri.nutricao.api.response.Response;
import com.danutri.nutricao.api.security.jwt.JwtTokenUtil;
import com.danutri.nutricao.api.service.UserService;
import com.mongodb.DuplicateKeyException;

@RestController
@RequestMapping("api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping()
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> create(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result) {
		
		Response<Usuario> response = new Response<Usuario>();
		
		try {
			validateCreateUser(usuario, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioPersistido = (Usuario) userService.createOrUpdate(usuario);
			response.setData(usuarioPersistido);
		}catch(DuplicateKeyException dE) {
			response.getErrors().add("Email já registrado no sistema");
			return ResponseEntity.badRequest().body(response);
		}catch(Exception ex) {
			response.getErrors().add(ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateCreateUser(Usuario usuario, BindingResult result) {
		if (usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuário", "Email não informado"));
			return;
		}
	}
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> update(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result) {
		
		Response<Usuario> response = new Response<Usuario>();
		
		try {
			validateUpdate(usuario, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioPersistido = (Usuario) userService.createOrUpdate(usuario);
			response.setData(usuarioPersistido);
		}catch(Exception ex) {
			response.getErrors().add(ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdate(Usuario usuario, BindingResult result) {
		if (usuario.getId() == null) {
			result.addError(new ObjectError("Usuário"," id do usuário não informado"));
			return;
		}
		if (usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuário", "Email não informado"));
			return;
		}
	}
	
	@GetMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Usuario>> findById(@PathVariable("id") String id) {
		Response<Usuario> response = new Response<Usuario>();
		Usuario usuario = userService.findById(id);
		if (usuario == null) {
			response.getErrors().add("Usuário não encontrado id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(usuario);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value="/{id}") 
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Usuario usuario = userService.findById(id);
		if (usuario == null) {
			response.getErrors().add("Usuário não encontrado id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		userService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	@GetMapping(value="/{page}/{count}")
	@PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
	public ResponseEntity<Response<Page<Usuario>>> findAll(HttpServletRequest request,@PathVariable int page, @PathVariable int count) {
		Response<Page<Usuario>> response = new Response<Page<Usuario>>();
		Usuario userRequest = userFromRequest(request);
		Page<Usuario> usuario = null;
		switch (userRequest.getPerfil()) {
		case ROLE_ADMIN:
			usuario = userService.findAll(page, count);	
			break;		
		case ROLE_CLIENTE:
			usuario = userService.findAllNutr(page, count);	
			break;
		}		
		response.setData(usuario);
		return ResponseEntity.ok(response);
	}
	
	private Usuario userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return userService.findByEmail(email);
	}
	
}
