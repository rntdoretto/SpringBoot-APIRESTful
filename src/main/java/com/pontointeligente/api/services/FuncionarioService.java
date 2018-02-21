package com.pontointeligente.api.services;


import java.util.List;

import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.response.Response;

public interface FuncionarioService {
	
	/**
	 *  Cadastra novo funcionario na base de dados.
	 * @param funcionario
	 * @return Funcionario
	 */
	Response<FuncionarioDTO> persistir(FuncionarioDTO funcionarioDto);
	
	/**
	 *  Busca e retorna um funcionario através do CPF.
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Response<FuncionarioDTO> buscarPorCpf(String cpf);
	
	/**
	 *  Busca e retorna um funcionario através do Email.
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Response<FuncionarioDTO> buscarPorEmail(String email);
	
	/**
	 *  Busca e retorna um funcionario através do Id.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Response<FuncionarioDTO> buscarPorId(Long id);
	
	/**
	 *  Busca e retorna uma lista de funcionários através do Id da Empresa.
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Response<List<FuncionarioDTO>> buscarPorEmpresaId(Long id);
	
	/**
	 * Remove um funcionário da base de dados.
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	Response<String> remover(Long id);

}
