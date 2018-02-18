package com.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.PessoaFisicaDTO;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.EmpresaServiceImpl;
import com.pontointeligente.api.services.impl.FuncionarioServiceImpl;
import com.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class PessoaFisicaController {

	private static final Logger log = LoggerFactory.getLogger(PessoaFisicaController.class);
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	public PessoaFisicaController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<PessoaFisicaDTO>> cadastrar(@Valid @RequestBody PessoaFisicaDTO cadastroPFDto,
			BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPFDto.toString());
		Response<PessoaFisicaDTO> response = new Response<PessoaFisicaDTO>();
		
//		validarDadosExistentes(cadastroPFDto, result);
		Funcionario funcionario = this.dataBindingDtoFuncionario(cadastroPFDto);
		
		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		//Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
		//empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		//this.funcionarioService.persistir(funcionario);
		
		response.setData(this.dataBindingFuncionarioParaDto(funcionario));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Verifica se a empresa ou funcinário já existem na base de dados.
	 * @param cadastroPFDto
	 * @param result
	 */
	/*private void validarDadosExistentes(PessoaFisicaDTO cadastroPFDto, BindingResult result) {
		this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
		this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe.")));
	}*/
	
	/**
	 * DataBinding de CadastroPFDTO com Funcionario
	 * @param cadastroPFDto
	 * @return Funcionario
	 */
	private Funcionario dataBindingDtoFuncionario(PessoaFisicaDTO cadastroPFDto) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDto.getNome());
		funcionario.setEmail(cadastroPFDto.getEmail());
		funcionario.setCpf(cadastroPFDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));
		cadastroPFDto.getQtdhorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDto.getQtdHorasTrabalhaDia()
				.ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		cadastroPFDto.getValorHora()
				.ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		
		return funcionario;
	}
	
	private PessoaFisicaDTO dataBindingFuncionarioParaDto(Funcionario funcionario) {
		PessoaFisicaDTO cadastroPFDto = new PessoaFisicaDTO();
		cadastroPFDto.setId(funcionario.getId());
		cadastroPFDto.setNome(funcionario.getNome());
		cadastroPFDto.setEmail(funcionario.getEmail());
		cadastroPFDto.setCpf(funcionario.getCpf());
		cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasAlmocoOpt()
			.ifPresent(qtdHorasAlmoco -> cadastroPFDto.setQtdhorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
			.ifPresent(qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhaDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
			.ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));
		
		return cadastroPFDto;
	}
}