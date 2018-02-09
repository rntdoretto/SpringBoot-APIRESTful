package com.pontointeligente.api.servicesimpl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.repositories.FuncionarioRepository;
import com.pontointeligente.api.services.IFuncionarioService;

@Service
public class FuncionarioService implements IFuncionarioService{

	private static final Logger log = LoggerFactory.getLogger(Funcionario.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepo;

	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionário: {}", funcionario);
		return this.funcionarioRepo.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buscando funcionario pelo CPF {}", cpf);
		return Optional.ofNullable(this.funcionarioRepo.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando funcionario pelo Email {}", email);
		return Optional.ofNullable(this.funcionarioRepo.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscando funcionario pelo ID {}", id);
		return Optional.ofNullable(this.funcionarioRepo.findOne(id));
	}
}
