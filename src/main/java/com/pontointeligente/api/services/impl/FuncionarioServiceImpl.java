package com.pontointeligente.api.services.impl;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

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
	public ResponseEntity<Response<FuncionarioDTO>> persistir(FuncionarioDTO funcionarioDto, BindingResult result) {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		
		if(funcionarioDto.getId() == 0 || funcionarioDto.getId() == null) {
			this.funcionarioIsPresent(funcionarioDto, result);
		}
		else {
			Optional<Funcionario> func = Optional.ofNullable(funcionarioRepository.findOne(funcionarioDto.getId()));
			if (func.isPresent()) {
				if(!func.get().getEmail().equals(funcionarioDto.getEmail()) || !func.get().getCpf().equals(funcionarioDto.getCpf())) {
					this.funcionarioIsPresent(funcionarioDto, result);
				}
			}
			else {
				result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
			}
		}
		
		Funcionario funcionario;
		try {
			funcionario = this.converterDtoParaFuncionario(funcionarioDto);
		} catch (NoSuchAlgorithmException e) {
			funcionario = new Funcionario();
			result.addError(new ObjectError("funcionario", "Erro ao gerar senha."));
		}
		
		if (result.hasErrors()) {
			log.error("Erro ao persistir funcionário. {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		log.info("Persistindo funcionário: {}", funcionario);
		this.funcionarioRepository.save(funcionario);
		
		response.setData(this.converterFuncionarioParaDto(funcionario));
		return ResponseEntity.ok(response);
	}

	@Override
	public Optional<Funcionario> buscarPorCpf(String cpf) {
		log.info("Buscando funcionario pelo CPF {}", cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscarPorEmail(String email) {
		log.info("Buscando funcionario pelo Email {}", email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscarPorId(Long id) {
		log.info("Buscando funcionario pelo ID {}", id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
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
	
	/**
	 * Verifica se funcinário já existem na base de dados.
	 * @param funcionarioDto
	 * @param result
	 */
	private void funcionarioIsPresent(FuncionarioDTO funcionarioDto, BindingResult result) {
		Optional<Funcionario> funcionario = Optional.ofNullable(funcionarioRepository.findByCpf(funcionarioDto.getCpf()));
		funcionario.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
		funcionario = Optional.ofNullable(funcionarioRepository.findByEmail(funcionarioDto.getEmail()));
		funcionario.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe.")));
	}
}
