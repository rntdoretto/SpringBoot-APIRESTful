package com.pontointeligente.api.controllers;

import java.text.ParseException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.LancamentoServiceImpl;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	
	@Autowired
	private LancamentoServiceImpl lancamentoService;
	
	@Value("${paginacao.qtdPorPagina}")
	private int qtdPorPagina;
	
	public LancamentoController() {
	}
	
	/**
	 * Retorna a listagem de lançamentos de um funcionário através de seu ID.
	 * @param idFuncionario
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return ResponseEntity<Response<Page<LancamentoDTO>>>
	 */
	@GetMapping(value = "/funcionario/{idFuncionario}")
	public ResponseEntity<Response<Page<LancamentoDTO>>> listarPorIdFuncionario(
			@PathVariable("idFuncionario") Long idFuncionario,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}. página: {}", idFuncionario, pag);
		Response<Page<LancamentoDTO>> response = lancamentoService.buscarPorFuncionarioId(idFuncionario, pag, ord, dir);
		
		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna um lançamento através de seu ID.
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDTO>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		return response(lancamentoService.buscaPorId(id));
	}
	
	/**
	 * Adiciona um novo lançamento
	 * @param lancamentoDto
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDTO>> adicionar(@Valid @RequestBody LancamentoDTO lancamentoDto, 
			BindingResult result) throws ParseException {
		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
		return response(lancamentoService.persistir(lancamentoDto));
	}
	
	/**
	 * Alterar lançamento através do seu ID.
	 * @param id
	 * @param lancamentoDto
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDTO lancamentoDto, BindingResult result) throws ParseException {
		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
		return response(lancamentoService.persistir(lancamentoDto));
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento ID: {}", id);
		Response<String> response = lancamentoService.remover(id);
		
		if (!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private ResponseEntity<Response<LancamentoDTO>> response(Response<LancamentoDTO> res){
		if (!res.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(res);
		}
		
		return ResponseEntity.ok(res);
	}
}
