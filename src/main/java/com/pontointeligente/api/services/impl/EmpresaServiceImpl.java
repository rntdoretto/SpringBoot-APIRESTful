package com.pontointeligente.api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;

import com.pontointeligente.api.dtos.EmpresaDTO;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.repositories.EmpresaRepository;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public ResponseEntity<Response<List<EmpresaDTO>>> buscarTodos() {
		Response<List<EmpresaDTO>> response = new Response<List<EmpresaDTO>>();
		log.info("Buscando uma empresas.");
		Optional<List<Empresa>> empresas = Optional.ofNullable(empresaRepository.findAll());

		if(!empresas.isPresent()) {
			log.info("Nenhuma empresa encontrada.");
			response.getErrors().add("Nenhuma empresa encontrada.");
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterEmpresasParaDto(empresas.get()));
		return ResponseEntity.ok(response);
	}
	
	@Override
	public ResponseEntity<Response<EmpresaDTO>> buscarPorId(Long id) {
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();
		log.info("Buscando uma empresa para o ID {}.", id);
		Optional<Empresa> empresa = Optional.ofNullable(empresaRepository.findOne(id));

		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o ID: {}", id);
			response.getErrors().add("Empresa não encontrada para o ID: {}" + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterEmpresaParaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<EmpresaDTO>> buscarPorCnpj(String cnpj) {
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();
		log.info("Buscando uma empresa para o CNPJ {}.", cnpj);
		Optional<Empresa> empresa = Optional.ofNullable(empresaRepository.findByCnpj(cnpj));

		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterEmpresaParaDto(empresa.get()));
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Response<EmpresaDTO>> persistir(EmpresaDTO empresaDto, BindingResult result) {
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();

		if (empresaDto.getId() == 0 || empresaDto.getId() == null) {
			this.empresaIsPresent(empresaDto, result);
		}
		else {
			Optional<Empresa> emp = Optional.ofNullable(this.empresaRepository.findOne(empresaDto.getId()));
			if(emp.isPresent()) {
				if (!emp.get().getCnpj().trim().equals(empresaDto.getCnpj().trim())) {
					this.empresaIsPresent(empresaDto, result);
				}
			}
			else {
				result.addError(new ObjectError("empresa", "Empresa não encontrado."));
			}
		}

		Empresa empresa = converterDtoParaEmpresa(empresaDto);

		if (result.hasErrors()) {
			log.error("Erro ao persistir empresa: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		log.info("Persistindo empresa: {}.", empresa.toString());
		this.empresaRepository.save(empresa);

		response.setData(this.converterEmpresaParaDto(empresa));
		return ResponseEntity.ok(response);
	}
	
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento ID: {}", id);
		Response<String> response = new Response<String>();
		Optional<Empresa> empresa = Optional.ofNullable(this.empresaRepository.findOne(id));
		
		if (!empresa.isPresent()) {
			log.info("Erro ao remover empresa ID: {} por ser inválido.", id);
			response.getErrors().add("Erro ao remover empresa. Registro não encontrado para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaRepository.delete(id);
		response.setData("Empresa removida ID: " + id);
		return ResponseEntity.ok(response);
	}

	/**
	 * Popular DTO com empresa
	 * @param empresa
	 * @return EmpresaDTO
	 */
	private EmpresaDTO converterEmpresaParaDto(Empresa empresa) {
		EmpresaDTO empresaDto = new EmpresaDTO();
		empresaDto.setId(empresa.getId());
		empresaDto.setCnpj(empresa.getCnpj());
		empresaDto.setRazaoSocial(empresa.getRazaoSocial());
		return empresaDto;
	}

	/**
	 * DataBinding de EmpresaDTO com Empresa
	 * @param empresaDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa(EmpresaDTO empresaDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(empresaDto.getCnpj());
		empresa.setRazaoSocial(empresaDto.getRazaoSocial());
		return empresa;
	}
	
	/**
	 * Popular List<DTO> com List<Empresa>
	 * @param empresa
	 * @return EmpresaDTO
	 */
	private List<EmpresaDTO> converterEmpresasParaDto(List<Empresa> empresas) {
		List<EmpresaDTO> empresasDto = new ArrayList<EmpresaDTO>();
		
		empresas.forEach(empresa -> {
			EmpresaDTO empresaDto = new EmpresaDTO();
			empresaDto.setId(empresa.getId());
			empresaDto.setCnpj(empresa.getCnpj());
			empresaDto.setRazaoSocial(empresa.getRazaoSocial());
			empresasDto.add(empresaDto);
		});
		
		return empresasDto;
	}

	/**
	 * Verifica se a empresa já existe na base de dados.
	 * @param empresaDto
	 * @param result
	 */
	private void empresaIsPresent(EmpresaDTO empresaDto, BindingResult result) {
		Optional<Empresa> empresa = Optional.ofNullable(this.empresaRepository.findByCnpj(empresaDto.getCnpj()));
		empresa.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
	}

}
