package com.pontointeligente.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.entities.Lancamento;
import com.pontointeligente.api.enums.TipoEnum;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.servicesimpl.FuncionarioServiceImpl;
import com.pontointeligente.api.servicesimpl.LancamentoServiceImpl;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoServiceImpl lancamentoService;
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	@Value("${paginacao.qtdPorPagina}")
	private int qtdPorPagina;
	
	public LancamentoController() {
	}
	
	/**
	 * Retorna a listagem de lançamentos de um funcionário através de seu ID.
	 * @param idFuncionario
	 * @param pag
	 * @param ord
	 * @param dir
	 * @return ResponseEntity<Response<Page<LancamentoDTO>>>
	 */
	@GetMapping(value = "/funcionario/{idFuncionario}")
	public ResponseEntity<Response<Page<LancamentoDTO>>> listarPorIdFuncionario(
			@PathVariable("idFuncionario") Long idFuncionario,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}. página: {}", idFuncionario, pag);
		Response<Page<LancamentoDTO>> response = new Response<Page<LancamentoDTO>>();
		
		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(idFuncionario, pageRequest);
		Page<LancamentoDTO> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoParaDto(lancamento));
		
		response.setData(lancamentosDto);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Retorna um lançamento através de seu ID.
	 * @param id
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDTO>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscaPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Lançamento não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento não encontrado para o ID "+ id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.converterLancamentoParaDto(lancamento.get()));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Adiciona um novo lançamento
	 * @param lancamentoDto
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Response<LancamentoDTO>> adicionar(@Valid @RequestBody LancamentoDTO lancamentoDto, 
			BindingResult result) throws ParseException {
		log.info("Adicionando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		this.validarFuncionario(lancamentoDto, result);
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoParaDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Alterar lançamento através do seu ID.
	 * @param id
	 * @param lancamentoDto
	 * @param result
	 * @return ResponseEntity<Response<LancamentoDTO>>
	 * @throws ParseException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<LancamentoDTO>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody LancamentoDTO lancamentoDto, BindingResult result) throws ParseException {
		log.info("Atualizando lançamento: {}", lancamentoDto.toString());
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		this.validarFuncionario(lancamentoDto, result);
		lancamentoDto.setId(Optional.of(id));
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoParaDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento ID: {}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscaPorId(id);
		
		if (!lancamento.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		this.lancamentoService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	/**
	 * Converte uma entidade lançamento para seu respectivo DTO.
	 * @param lancamento
	 * @return LancamentoDTO
	 */
	private LancamentoDTO converterLancamentoParaDto(Lancamento lancamento) {
		LancamentoDTO lancamentoDto = new LancamentoDTO();
		lancamentoDto.setId(Optional.of(lancamento.getId()));
		lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentoDto.setTipo(lancamento.getTipo().toString());
		lancamentoDto.setDescricao(lancamento.getDescricao());
		lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
		lancamentoDto.setIdFuncionario(lancamento.getFuncionario().getId());
		return lancamentoDto;
	}
	
	/**
	 * Converte um DTO lançamento para sua respectiva Entidade.
	 * @param lancamentoDto
	 * @param result
	 * @return Lancamento
	 * @throws ParseException
	 */
	private Lancamento converterDtoParaLancamento(LancamentoDTO lancamentoDto, BindingResult result) throws ParseException {
		Lancamento lancamento = new Lancamento();
		
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscaPorId(lancamentoDto.getId().get());
			if(lanc.isPresent()) {
				lancamento = lanc.get();
			}
			else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		}
		else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getIdFuncionario());
		}
		
		lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));
		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		
		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		}
		else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}
		return lancamento;
	}
	
	/**
	 * Valida um funcionário, verificando se existe.
	 * @param lancamentoDto
	 * @param result
	 */
	private void validarFuncionario(LancamentoDTO lancamentoDto, BindingResult result) {
		if(lancamentoDto.getIdFuncionario() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado."));
			return;
		}
		
		log.info("Validando funcionário ID: {}.", lancamentoDto.getIdFuncionario());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getIdFuncionario());
		if(!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
		}
	}
}
