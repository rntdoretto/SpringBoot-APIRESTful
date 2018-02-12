package com.pontointeligente.api.services;

import java.util.Optional;

import com.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {
	
	/**
	 *  Cadastra novo funcionario na base de dados.
	 * @param funcionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 *  Busca e retorna um funcionario através do CPF.
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	/**
	 *  Busca e retorna um funcionario através do Email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorEmail(String email);
	
	/**
	 *  Busca e retorna um funcionario através do Id.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscarPorId(Long id);

}
