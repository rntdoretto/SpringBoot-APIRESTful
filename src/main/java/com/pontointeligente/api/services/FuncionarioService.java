package com.pontointeligente.api.services;

import java.util.Optional;

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
