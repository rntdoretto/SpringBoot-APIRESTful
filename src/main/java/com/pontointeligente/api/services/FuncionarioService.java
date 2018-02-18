package com.pontointeligente.api.services;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.response.Response;

public interface FuncionarioService {
	
	/**
	 *  Cadastra novo funcionario na base de dados.
	 * @param funcionario
	 * @return Funcionario
	 */
	ResponseEntity<Response<FuncionarioDTO>> persistir(FuncionarioDTO funcionarioDto, BindingResult result);
	
	/**
	 *  Busca e retorna um funcionario através do CPF.
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	ResponseEntity<Response<FuncionarioDTO>> buscarPorCpf(String cpf);
	
	/**
	 *  Busca e retorna um funcionario através do Email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	ResponseEntity<Response<FuncionarioDTO>> buscarPorEmail(String email);
	
	/**
	 *  Busca e retorna um funcionario através do Email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Funcionario buscarParaAutenticacao(String email);
	
	/**
	 *  Busca e retorna um funcionario através do Id.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	ResponseEntity<Response<FuncionarioDTO>> buscarPorId(Long id);

}
