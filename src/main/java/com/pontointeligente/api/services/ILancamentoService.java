package com.pontointeligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.pontointeligente.api.entities.Lancamento;

public interface ILancamentoService {

	/**
	 * Retorna uma lista paginada de lançamentos através de um funcionário.
	 * @param idFuncionario
	 * @param pageRequest
	 * @return Page<Lancamento>
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long idFuncionario, PageRequest pageRequest);
	
	/**
	 * Retorna um lançamento por ID.
	 * @param id
	 * @return Optional<Lancamento>
	 */
	Optional<Lancamento> buscaPorId(Long id);
	
	/**
	 * Persuste um lançamento na base de dados.
	 * @param lancamento
	 * @return Lancamento
	 */
	Lancamento persistir(Lancamento lancamento);
	
	/**
	 * Remove um lançamento da base de dados.
	 * @param id
	 */
	void remover(Long id);
	
}
