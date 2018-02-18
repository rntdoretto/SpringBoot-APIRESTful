package com.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

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

import com.pontointeligente.api.dtos.PessoaJuridicaDTO;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.EmpresaServiceImpl;
import com.pontointeligente.api.services.impl.FuncionarioServiceImpl;
import com.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class PessoaJuridicaController {

	private static final Logger log = LoggerFactory.getLogger(PessoaJuridicaController.class);
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	@Autowired
	private EmpresaServiceImpl empresaService;
	
	public PessoaJuridicaController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<PessoaJuridicaDTO>> cadastrar(@Valid @RequestBody 
			PessoaJuridicaDTO pessoaJuridicaDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PJ: {}", pessoaJuridicaDto.toString());
		
		Response<PessoaJuridicaDTO> response = new Response<PessoaJuridicaDTO>();
		
//		this.validarDadosExistentes(pessoaJuridicaDto, result);
		Empresa empresa = this.dataBindingDtoEmpresa(pessoaJuridicaDto);
		Funcionario funcionario = this.dataBindingDtoFuncionario(pessoaJuridicaDto);
		
		if (result.hasErrors()) {
			log.error("Erro ao validar dados do cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		//this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		//this.funcionarioService.persistir(funcionario);
		
		response.setData(this.dataBindingFuncionarioParaDto(funcionario));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Verifica se a empresa ou funcinário já existem na base de dados.
	 * @param pessoaJuridicaDto
	 * @param result
	 */
	/*private void validarDadosExistentes(PessoaJuridicaDTO pessoaJuridicaDto, BindingResult result) {
		this.funcionarioService.buscarPorCpf(pessoaJuridicaDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
		this.funcionarioService.buscarPorEmail(pessoaJuridicaDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe.")));
	}*/
	
	/**
	 * DataBinding de CadastroPJDTO com Empresa
	 * @param pessoaJuridicaDto
	 * @return Empresa
	 */
	private Empresa dataBindingDtoEmpresa(PessoaJuridicaDTO pessoaJuridicaDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(pessoaJuridicaDto.getCnpj());
		empresa.setRazaoSocial(pessoaJuridicaDto.getRazaoSocial());
		
		return empresa;
	}
	
	/**
	 * DataBinding de CadastroPJDTO com Funcionario
	 * @param pessoaJuridicaDto
	 * @return Funcionario
	 */
	private Funcionario dataBindingDtoFuncionario(PessoaJuridicaDTO pessoaJuridicaDto) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(pessoaJuridicaDto.getNome());
		funcionario.setEmail(pessoaJuridicaDto.getEmail());
		funcionario.setCpf(pessoaJuridicaDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(pessoaJuridicaDto.getSenha()));
		
		return funcionario;
	}
	
	private PessoaJuridicaDTO dataBindingFuncionarioParaDto(Funcionario funcionario) {
		PessoaJuridicaDTO cadastroPJDto = new PessoaJuridicaDTO();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastroPJDto;
	}
}
