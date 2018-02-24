package com.pontointeligente.api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pontointeligente.api.audit.documents.Audit;
import com.pontointeligente.api.audit.repositories.AuditRepository;
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
	
	@Autowired
	private AuditRepository auditRepository;

	@Override
	public Response<List<EmpresaDTO>> buscarTodos() {
		log.info("Buscando todas empresas.");
		auditRepository.save(new Audit("PontoInteligente", "10.0.0.1", "Buscando todas empresas."));
		Response<List<EmpresaDTO>> response = new Response<List<EmpresaDTO>>();
		Optional<List<Empresa>> empresas = Optional.ofNullable(empresaRepository.findAll());

		if(!empresas.isPresent()) {
			log.info("Nenhuma empresa encontrada.");
			response.getErrors().add("Nenhuma empresa encontrada.");
			return response;
		}

		response.setData(this.converterEmpresasParaDto(empresas.get()));
		
		return response;
	}
	
	@Override
	public Response<EmpresaDTO> buscarPorId(Long id) {
		log.info("Buscando uma empresa para o ID: {}.", id);
		auditRepository.save(new Audit("PontoInteligente", "10.0.0.1", "Buscando uma empresa para o ID: " + id));
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();
		Optional<Empresa> empresa = Optional.ofNullable(empresaRepository.findOne(id));

		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o ID: {}", id);
			response.getErrors().add("Empresa não encontrada para o ID: {}" + id);
			return response;
		}

		response.setData(this.converterEmpresaParaDto(empresa.get()));
		
		return response;
	}

	@Override
	public Response<EmpresaDTO> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa para o CNPJ {}.", cnpj);
		auditRepository.save(new Audit("PontoInteligente", "10.0.0.1", "Buscando uma empresa para o CNPJ: " + cnpj));
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();
		Optional<Empresa> empresa = Optional.ofNullable(empresaRepository.findByCnpj(cnpj));

		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ " + cnpj);
			return response;
		}

		response.setData(this.converterEmpresaParaDto(empresa.get()));
		return response;
	}

	@Override
	public Response<EmpresaDTO> persistir(EmpresaDTO empresaDto) {
		auditRepository.save(new Audit("PontoInteligente", "10.0.0.1", "Persistindo empresa."));
		Response<EmpresaDTO> response = new Response<EmpresaDTO>();

		if (empresaDto.getId() == null) {
			this.empresaIsPresent(empresaDto, response);
		}
		else {
			Optional<Empresa> emp = Optional.ofNullable(this.empresaRepository.findOne(empresaDto.getId()));
			if(emp.isPresent()) {
				if (!emp.get().getCnpj().trim().equals(empresaDto.getCnpj().trim())) {
					this.empresaIsPresent(empresaDto, response);
				}
			}
			else {
				response.getErrors().add("Empresa não encontrado.");
			}
		}
		
		if (!response.getErrors().isEmpty()) {
			log.error("Erro ao persistir empresa: {}", response.getErrors());
			return response;
		}

		Empresa empresa = converterDtoParaEmpresa(empresaDto);

		log.info("Persistindo empresa: {}.", empresa.toString());
		this.empresaRepository.save(empresa);

		response.setData(this.converterEmpresaParaDto(empresa));
		return response;
	}
	
	public Response<String> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento ID: {}", id);
		auditRepository.save(new Audit("PontoInteligente", "10.0.0.1", "Removendo lançamento ID: " + id));
		Response<String> response = new Response<String>();
		Optional<Empresa> empresa = Optional.ofNullable(this.empresaRepository.findOne(id));
		
		if (!empresa.isPresent()) {
			log.error("Erro ao remover empresa ID: {} por ser inválido.", id);
			response.getErrors().add("Erro ao remover empresa. Registro não encontrado para o ID " + id);
			return response;
		}
		
		this.empresaRepository.delete(id);
		response.setData("Empresa removida ID: " + id);
		return response;
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
	private void empresaIsPresent(EmpresaDTO empresaDto, Response<EmpresaDTO> response) {
		Optional<Empresa> empresa = Optional.ofNullable(this.empresaRepository.findByCnpj(empresaDto.getCnpj()));
		empresa.ifPresent(emp -> response.getErrors().add("Empresa já existente."));
	}

}
