package com.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@PostMapping
	public ResponseEntity<Response<FuncionarioDTO>> inserir(
			@Valid @RequestBody FuncionarioDTO funcionarioDto, BindingResult result) {
		log.info("Inserindo funcionário: {}", funcionarioDto.toString());
		return funcionarioService.persistir(funcionarioDto, result);
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<Response<FuncionarioDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDTO funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando funcionário: {}", funcionarioDto.toString());
		funcionarioDto.setId(id);
		return funcionarioService.persistir(funcionarioDto, result);
	}
}
