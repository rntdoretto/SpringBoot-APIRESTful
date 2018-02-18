package com.pontointeligente.api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.pontointeligente.api.dtos.EmpresaDTO;
import com.pontointeligente.api.response.Response;

public interface EmpresaService {
	
	
	/**
	 * Retorna todas empresas
	 * @return ResponseEntity<Response<EmpresaDTO>>
	 */
	ResponseEntity<Response<List<EmpresaDTO>>> buscarTodos();

	/**
	 * Retorna uma empresa através de um ID.
	 * @param id
	 * @return ResponseEntity<Response<EmpresaDTO>>
	 */
	ResponseEntity<Response<EmpresaDTO>> buscarPorId(Long id);
	
	/**
	 * Retorna uma empresa através de um CNPJ.
	 * @param cnpj
	 * @return ResponseEntity<Response<EmpresaDTO>>
	 */
	ResponseEntity<Response<EmpresaDTO>> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa na base de dados.
	 * @param empresaDto, result
	 * @return Empresa
	 */
	ResponseEntity<Response<EmpresaDTO>> persistir(EmpresaDTO empresaDto, BindingResult result);
	
	/**
	 * Remove um empresa da base de dados.
	 * @param id
	 * @return ResponseEntity<Response<String>>
	 */
	ResponseEntity<Response<String>> remover(Long id);
	
}
