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

import com.pontointeligente.api.dtos.CadastroPJDTO;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.servicesimpl.EmpresaServiceImpl;
import com.pontointeligente.api.servicesimpl.FuncionarioServiceImpl;
import com.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	@Autowired
	private EmpresaServiceImpl empresaService;
	
	public CadastroPJController() {
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDTO>> cadastrar(@Valid @RequestBody 
			CadastroPJDTO cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		
		Response<CadastroPJDTO> response = new Response<CadastroPJDTO>();
		
		this.validarDadosExistentes(cadastroPJDto, result);
		Empresa empresa = this.dataBindingDtoEmpresa(cadastroPJDto);
		Funcionario funcionario = this.dataBindingDtoFuncionario(cadastroPJDto);
		
		if (result.hasErrors()) {
			log.error("Erro ao validar dados do cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.dataBindingFuncionarioParaDto(funcionario));
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Verifica se a empresa ou funcinário já existem na base de dados.
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDTO cadastroPJDto, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDto.getCnpj())
			.ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));
		this.funcionarioService.buscarPorCpf(cadastroPJDto.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existe.")));
		this.funcionarioService.buscarPorEmail(cadastroPJDto.getEmail())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existe.")));
	}
	
	/**
	 * DataBinding de CadastroPJDTO com Empresa
	 * @param cadastroPJDto
	 * @return Empresa
	 */
	private Empresa dataBindingDtoEmpresa(CadastroPJDTO cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());
		
		return empresa;
	}
	
	/**
	 * DataBinding de CadastroPJDTO com Funcionario
	 * @param cadastroPJDto
	 * @return Funcionario
	 */
	private Funcionario dataBindingDtoFuncionario(CadastroPJDTO cadastroPJDto) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));
		
		return funcionario;
	}
	
	private CadastroPJDTO dataBindingFuncionarioParaDto(Funcionario funcionario) {
		CadastroPJDTO cadastroPJDto = new CadastroPJDTO();
		cadastroPJDto.setId(funcionario.getId());
		cadastroPJDto.setNome(funcionario.getNome());
		cadastroPJDto.setEmail(funcionario.getEmail());
		cadastroPJDto.setCpf(funcionario.getCpf());
		cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		return cadastroPJDto;
	}
}
