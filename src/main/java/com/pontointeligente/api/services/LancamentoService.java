package com.pontointeligente.api.services;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.response.Response;

public interface LancamentoService {

	/**
	 * Retorna uma lista paginada de lançamentos através de um funcionário.
	 * @param idFuncionario
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	ResponseEntity<Response<Page<LancamentoDTO>>> buscarPorFuncionarioId(Long idFuncionario, int pag, String ord, String dir);
	
	/**
	 * Retorna um lançamento por ID.
	 * @param id
	 * @return Optional<Lancamento>
	 */
	ResponseEntity<Response<LancamentoDTO>> buscaPorId(Long id);
	
	/**
	 * Persuste um lançamento na base de dados.
	 * @param lancamento
	 * @return Lancamento
	 */
	ResponseEntity<Response<LancamentoDTO>> persistir(LancamentoDTO lancamentoDto, BindingResult result);
	
	/**
	 * Remove um lançamento da base de dados.
	 * @param id
	 */
	ResponseEntity<Response<String>> remover(Long id);
	
}
