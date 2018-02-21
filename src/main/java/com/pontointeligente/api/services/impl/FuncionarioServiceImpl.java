package com.pontointeligente.api.services.impl;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.repositories.FuncionarioRepository;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.FuncionarioService;
import com.pontointeligente.api.utils.PasswordUtils;

@Service
public class FuncionarioServiceImpl implements FuncionarioService{

	private static final Logger log = LoggerFactory.getLogger(Funcionario.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Override
	public Response<FuncionarioDTO> persistir(FuncionarioDTO funcionarioDto) {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		
		if(funcionarioDto.getId() == null) {
			this.funcionarioIsPresent(funcionarioDto, response);
		}
		else {
			Optional<Funcionario> func = Optional.ofNullable(this.funcionarioRepository.findOne(funcionarioDto.getId()));
			if (func.isPresent()) {
				if(!func.get().getEmail().equals(funcionarioDto.getEmail()) || !func.get().getCpf().equals(funcionarioDto.getCpf())) {
					this.funcionarioIsPresent(funcionarioDto, response);
				}
			}
			else {
				response.getErrors().add("Funcionário não encontrado.");
			}
		}
		
		Funcionario funcionario;
		try {
			funcionario = this.converterDtoParaFuncionario(funcionarioDto);
		} catch (NoSuchAlgorithmException e) {
			funcionario = new Funcionario();
			response.getErrors().add("Erro ao gerar a senha.");
		}
		
		if (!response.getErrors().isEmpty()) {
			log.error("Erro ao persistir funcionário. {}", response.getErrors());
			return response;
		}
		
		log.info("Persistindo funcionário: {}", funcionario);
		this.funcionarioRepository.save(funcionario);
		
		response.setData(this.converterFuncionarioParaDto(funcionario));
		return response;
	}

	@Override
	public Response<FuncionarioDTO> buscarPorCpf(String cpf) {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		log.info("Buscando funcionario pelo CPF {}", cpf);
		Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
		
		if(!funcionario.isPresent()) {
			log.info("Funcionario não encontrado para o CPF: {}", cpf);
			response.getErrors().add("Funcionario não encontrado para o CPF: {}" + cpf);
			return response;
		}
		
		response.setData(this.converterFuncionarioParaDto(funcionario.get()));
		return response;
	}

	@Override
	public Response<FuncionarioDTO> buscarPorEmail(String email) {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		log.info("Buscando funcionario pelo Email {}", email);
		Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
		
		if(!funcionario.isPresent()) {
			log.info("Funcionario não encontrado para o Email: {}", email);
			response.getErrors().add("Funcionario não encontrado para o Email: {}" + email);
			return response;
		}
		
		response.setData(this.converterFuncionarioParaDto(funcionario.get()));
		return response;
	}

	@Override
	public Response<FuncionarioDTO> buscarPorId(Long id) {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		log.info("Buscando funcionario pelo ID {}", id);
		Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findOne(id));
		
		if(!funcionario.isPresent()) {
			log.info("Funcionario não encontrado para o ID: {}", id);
			response.getErrors().add("Funcionario não encontrado para o ID: {}" + id);
			return response;
		}
		
		response.setData(this.converterFuncionarioParaDto(funcionario.get()));
		return response;
	}
	
	@Override
	public Response<List<FuncionarioDTO>> buscarPorEmpresaId(Long idEmpresa) {
		Response<List<FuncionarioDTO>> response = new Response<List<FuncionarioDTO>>();
		
		Optional<List<Funcionario>> funcionarios = Optional.ofNullable(this.funcionarioRepository.findByEmpresaId(idEmpresa));
		
		if(!funcionarios.isPresent()) {
			log.info("Nenhum funcionario encontrado para a empresa ID: {}", idEmpresa);
			response.getErrors().add("Funcionario não encontrado para o ID: {}" + idEmpresa);
			return response;
		}
		
		response.setData(this.converterFuncionariosParaDto(funcionarios.get()));
		return response;
	}
	
	@Override
	public Response<String> remover(@PathVariable("id") Long id) {
		log.info("Removendo funcionário ID: {}", id);
		Response<String> response = new Response<String>();
		Optional<Funcionario> funcionario = Optional.ofNullable(this.funcionarioRepository.findOne(id));
		
		if(!funcionario.isPresent()) {
			log.info("Funcionario não encontrado para o ID: {}", id);
			response.getErrors().add("Funcionario não encontrado para o ID: {}" + id);
			return response;
		}
		
		this.funcionarioRepository.delete(id);
		response.setData("Funcionário removido ID: " + id);
		return response;
	}
	
	/**
	 * DataBinding de CadastroPFDTO com Funcionario
	 * @param funcionarioDto
	 * @return Funcionario
	 */
	private Funcionario converterDtoParaFuncionario(FuncionarioDTO funcionarioDto) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(funcionarioDto.getNome());
		funcionario.setEmail(funcionarioDto.getEmail());
		funcionario.setCpf(funcionarioDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionarioDto.getSenha()
				.ifPresent(senha -> funcionario.setSenha(PasswordUtils.gerarBCrypt(senha)));
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		funcionarioDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		funcionarioDto.getValorHora()
				.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
	}
	
	private FuncionarioDTO converterFuncionarioParaDto(Funcionario funcionario) {
		FuncionarioDTO funcionarioDto = new FuncionarioDTO();
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setNome(funcionario.getNome());
		funcionarioDto.setEmail(funcionario.getEmail());
		funcionarioDto.setCpf(funcionario.getCpf());
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
			.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));
		
		return funcionarioDto;
	}
	
	private List<FuncionarioDTO> converterFuncionariosParaDto(List<Funcionario> funcionarios) {
		List<FuncionarioDTO> funcionariosDto = new ArrayList<FuncionarioDTO>();
		
		funcionarios.forEach(funcionario -> {
			FuncionarioDTO funcionarioDto = new FuncionarioDTO();
			funcionarioDto.setId(funcionario.getId());
			funcionarioDto.setNome(funcionario.getNome());
			funcionarioDto.setEmail(funcionario.getEmail());
			funcionarioDto.setCpf(funcionario.getCpf());
			funcionario.getQtdHorasAlmocoOpt()
				.ifPresent(qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
			funcionario.getQtdHorasTrabalhoDiaOpt()
				.ifPresent(qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
			funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));
			funcionariosDto.add(funcionarioDto);
		});
		
		return funcionariosDto;
	}
	
	/**
	 * Verifica se funcinário já existem na base de dados.
	 * @param funcionarioDto
	 * @param result
	 */
	private void funcionarioIsPresent(FuncionarioDTO funcionarioDto, Response<FuncionarioDTO> response) {
		Optional<Funcionario> funcionario = Optional.ofNullable(funcionarioRepository.findByCpf(funcionarioDto.getCpf()));
		funcionario.ifPresent(func -> response.getErrors().add("CPF já existe."));
		funcionario = Optional.ofNullable(funcionarioRepository.findByEmail(funcionarioDto.getEmail()));
		funcionario.ifPresent(func -> response.getErrors().add("Email já existe."));
	}
}
