package com.pontointeligente.api.controllers;

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

import com.pontointeligente.api.dtos.EmpresaDTO;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.EmpresaServiceImpl;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	@Autowired
	private EmpresaServiceImpl empresaService;
	
	public EmpresaController() {
	}
	
	@GetMapping
	public ResponseEntity<Response<List<EmpresaDTO>>> buscarTodos() {
		log.info("Buscando empresas.");
		return empresaService.buscarTodos();
	}
	
	@GetMapping(value = "/id/{id}")
	public ResponseEntity<Response<EmpresaDTO>> buscarPorId(@PathVariable("id") Long id) {
		log.info("Buscando empresa por ID: {}", id);
		return empresaService.buscarPorId(id);
	}
	
	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDTO>> buscarPorCnpj(@PathVariable("cnpj") String cnpj) {
		log.info("Buscando empresa por CNPJ: {}", cnpj);
		return empresaService.buscarPorCnpj(cnpj);
	}
	
	@PostMapping
	public ResponseEntity<Response<EmpresaDTO>> inserir(@Valid @RequestBody EmpresaDTO dto, BindingResult result) {
		log.info("Inserindo empresa: {}", dto.toString());
		return empresaService.persistir(dto, result);
	}
	
	@PutMapping(value = "{id}")
	public ResponseEntity<Response<EmpresaDTO>> atualizar(@PathVariable("id") Long id, 
			@Valid @RequestBody EmpresaDTO dto, BindingResult result) {
		log.info("Atualizando empresa: {}", dto.toString());
		dto.setId(id);
		return empresaService.persistir(dto, result);
	}
	
	@DeleteMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo empresa ID: {}", id);
		return empresaService.remover(id);
	}
}
