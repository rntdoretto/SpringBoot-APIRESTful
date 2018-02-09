package com.pontointeligente.api.servicesimpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.repositories.EmpresaRepository;
import com.pontointeligente.api.services.IEmpresaService;

@Service
public class EmpresaService implements IEmpresaService {

	private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);
	
	@Autowired
	private EmpresaRepository empresaRepo;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa para o CNPJ {}.", cnpj);
		return Optional.ofNullable(empresaRepo.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Persistindo empresa: {}.", empresa);
		return this.empresaRepo.save(empresa);
	}
	
}
