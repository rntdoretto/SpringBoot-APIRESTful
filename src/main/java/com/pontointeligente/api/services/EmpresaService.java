package com.pontointeligente.api.services;

import java.util.List;

import com.pontointeligente.api.dtos.EmpresaDTO;
import com.pontointeligente.api.response.Response;

public interface EmpresaService {
	
	
	/**
	 * Retorna todas empresas
	 * @return Response<List<EmpresaDTO>>
	 */
	Response<List<EmpresaDTO>> buscarTodos();

	/**
	 * Retorna uma empresa através de um ID.
	 * @param id
	 * @return Response<EmpresaDTO>
	 */
	Response<EmpresaDTO> buscarPorId(Long id);
	
	/**
	 * Retorna uma empresa através de um CNPJ.
	 * @param cnpj
	 * @return Response<EmpresaDTO>
	 */
	Response<EmpresaDTO> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa na base de dados.
	 * @param empresaDto, result
	 * @return Response<EmpresaDTO>
	 */
	Response<EmpresaDTO> persistir(EmpresaDTO empresaDto);
	
	/**
	 * Remove um empresa da base de dados.
	 * @param id
	 * @return Response<String>
	 */
	Response<String> remover(Long id);
	
}
