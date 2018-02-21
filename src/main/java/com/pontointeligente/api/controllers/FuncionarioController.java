package com.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.FuncionarioServiceImpl;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {
	
	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;

	public FuncionarioController() {
	}
	
	@GetMapping(value = "/email/{email}")
	public ResponseEntity<Response<FuncionarioDTO>> buscarPorEmail(@PathVariable("email") String email) {
		log.info("Buscando funcionário por Email: {}", email);
		return response(funcionarioService.buscarPorEmail(email));
	}
	
	@GetMapping(value = "/cpf/{cpf}")
	public ResponseEntity<Response<FuncionarioDTO>> buscarPorCpf(@PathVariable("cpf") String cpf) {
		log.info("Buscando funcionário por CPF: {}", cpf);
		return response(funcionarioService.buscarPorCpf(cpf));
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Response<FuncionarioDTO>> buscarPorId(@PathVariable("id") Long id) {
		log.info("Buscando funcionário por ID: {}", id);
		return response(funcionarioService.buscarPorId(id));
	}
	
	@GetMapping(value = "/idEmpresa/{id}")
	public ResponseEntity<Response<List<FuncionarioDTO>>> buscarPorEmpresaId(@PathVariable("id") Long id) {
		log.info("Buscando funcionários por empresa ID: {}", id);
		Response<List<FuncionarioDTO>> response = funcionarioService.buscarPorEmpresaId(id);
		
		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping
	public ResponseEntity<Response<FuncionarioDTO>> inserir(
			@Valid @RequestBody FuncionarioDTO funcionarioDto, BindingResult result) {
		log.info("Inserindo funcionário: {}", funcionarioDto.toString());
		return response(funcionarioService.persistir(funcionarioDto));
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<Response<FuncionarioDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDTO funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", funcionarioDto.toString());
		funcionarioDto.setId(id);
		return response(funcionarioService.persistir(funcionarioDto));
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo funcionário ID: {}", id);
		Response<String> response = funcionarioService.remover(id);
		
		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private ResponseEntity<Response<FuncionarioDTO>> response(Response<FuncionarioDTO> res){
		if (!res.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(res);
		}
		
		return ResponseEntity.ok(res);
	}
}
