package com.pontointeligente.api.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.entities.Lancamento;
import com.pontointeligente.api.enums.TipoEnum;
import com.pontointeligente.api.repositories.LancamentoRepository;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.LancamentoService;

import springfox.documentation.annotations.Cacheable;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(Lancamento.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private LancamentoRepository lancamentoRepository;

	@Value("${paginacao.qtdPorPagina}")
	private int qtdPorPagina;

	@Override
	public Response<Page<LancamentoDTO>> buscarPorFuncionarioId(Long idFuncionario, int pag, String ord, String dir) {
		log.info("Buscando lançamentos para o funcionário ID:{}", idFuncionario);

		Response<Page<LancamentoDTO>> response = new Response<Page<LancamentoDTO>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Optional<Page<Lancamento>> lancamentos = Optional.ofNullable(this.lancamentoRepository.findByFuncionarioId(idFuncionario, pageRequest));
		
		if (!lancamentos.isPresent()) {
			log.info("Nenhum lançamento encontrado para o funcionário ID: {}", idFuncionario);
			response.getErrors().add("Nenhum lançamento encontrado para o funcionário.");
			return response;
		}
		
		Page<LancamentoDTO> lancamentosDto = lancamentos.get().map(lancamento -> this.converterLancamentoParaDto(lancamento));

		response.setData(lancamentosDto);
		return response;
	}

	@Override
	@Cacheable("lancamentoPorId")
	public Response<LancamentoDTO> buscaPorId(Long id) {
		log.info("Buscando um lançamento pelo ID:{}", id);
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		Optional<Lancamento> lancamento = Optional.ofNullable(this.lancamentoRepository.findOne(id));
		
		if(!lancamento.isPresent()) {
			log.info("Lançamento não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento não encontrado para o ID "+ id);
			return response;
		}
		
		response.setData(this.converterLancamentoParaDto(lancamento.get()));
		return response;
	}

	@Override
	@CachePut("lancamentoPorId")
	public Response<LancamentoDTO> persistir(LancamentoDTO lancamentoDto) {
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		
		Lancamento lancamento;
		try {
			lancamento = this.converterDtoParaLancamento(lancamentoDto, response);
		} catch (ParseException e) {
			lancamento = new Lancamento();
			response.getErrors().add("Erro de data: " + e.getMessage());
		}
		
		if(!response.getErrors().isEmpty()) {
			log.error("Erro validando lançamento: {}", response.getErrors());
			response.getErrors().add("Erro validando lançamento.");
			return response;
		}
		
		lancamento = this.lancamentoRepository.save(lancamento);
		response.setData(this.converterLancamentoParaDto(lancamento));
		return response;
	}

	@Override
	public Response<String> remover(Long id) {
		log.info("Excluindo o lançamento ID:{}", id);
		Response<String> response = new Response<String>();
		Optional<Lancamento> lancamento = Optional.ofNullable(this.lancamentoRepository.findOne(id));
		
		if (!lancamento.isPresent()) {
			log.info("Erro ao remover lançamento ID: {} por ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o ID: " + id);
			return response;
		}
		
		this.lancamentoRepository.delete(id);
		response.setData("Lancamento removido ID: " + id);
		return response;
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
	private Lancamento converterDtoParaLancamento(LancamentoDTO lancamentoDto, Response<LancamentoDTO> response) throws ParseException {
		Lancamento lancamento = new Lancamento();
		
		if(lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = Optional.ofNullable(this.lancamentoRepository.findOne(lancamentoDto.getId().get()));
			if(lanc.isPresent()) {
				lancamento = lanc.get();
			}
			else {
				response.getErrors().add("Lançamento não encontrado.");
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
			response.getErrors().add("Tipo inválido.");
		}
		return lancamento;
	}

}
