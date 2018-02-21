package com.pontointeligente.api.services;

import org.springframework.data.domain.Page;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.response.Response;

public interface LancamentoService {

	/**
	 * Retorna uma lista paginada de lançamentos através de um funcionário.
	 * @param idFuncionario
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Response<Page<LancamentoDTO>> buscarPorFuncionarioId(Long idFuncionario, int pag, String ord, String dir);
	
	/**
	 * Retorna um lançamento por ID.
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Response<LancamentoDTO> buscaPorId(Long id);
	
	/**
	 * Persuste um lançamento na base de dados.
	 * @param lancamento
	 * @return Lancamento
	 */
	Response<LancamentoDTO> persistir(LancamentoDTO lancamentoDto);
	
	/**
	 * Remove um lançamento da base de dados.
	 * @param id
	 */
	Response<String> remover(Long id);
	
}
