package com.pontointeligente.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.enums.TipoEnum;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.FuncionarioServiceImpl;
import com.pontointeligente.api.services.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LancamentoControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LancamentoServiceImpl lancamentoService;
	
	@MockBean
	private FuncionarioServiceImpl funcionarioService;
	
	private static final String URL_BASE = "/api/lancamentos/";
	private static final String	URL_BUSCA_FUNCIONARIO_ID = "/api/funcionarios/id/";
	private static final Long ID_FUNCIONARIO = 1L;
	private static final Long ID_LANCAMENTO = 1L;
	private static final String TIPO = TipoEnum.INICIO_TRABALHO.name();
	private static final Date DATA = new Date();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	@WithMockUser
	public void testCadastrarLancamento() throws Exception {
		BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong()))
				.willReturn(this.obterDadosFuncionario());
		BDDMockito.given(this.lancamentoService.persistir(Mockito.any(LancamentoDTO.class)))
				.willReturn(this.obterDadosLancamento());
		
		mvc.perform(MockMvcRequestBuilders
				.post(URL_BASE)
				.content(this.obterJsonRequestPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO))
				.andExpect(jsonPath("$.data.tipo").value(TIPO))
				.andExpect(jsonPath("$.data.data").value(this.dateFormat.format(DATA)))
				.andExpect(jsonPath("$.data.idFuncionario").value(ID_FUNCIONARIO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	@WithMockUser
	public void testCadastrarLancamentoIdFuncionarioInvalido() throws Exception {
		BDDMockito.given(this.funcionarioService.buscarPorId(Mockito.anyLong()))
				.willReturn(this.funcionarioInvalido());
		
		mvc.perform(MockMvcRequestBuilders
				.get(URL_BUSCA_FUNCIONARIO_ID + ID_FUNCIONARIO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	@WithMockUser(username = "admin@doretto.com", roles = {"ADMIN"})
	public void testRemoverLancamento() throws Exception {
		BDDMockito.given(this.lancamentoService.buscaPorId(Mockito.anyLong()))
				.willReturn(this.obterDadosLancamento());
		
		mvc.perform(MockMvcRequestBuilders
				.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testRemoverLancamentoAcessoNegado() throws Exception {
		BDDMockito.given(this.lancamentoService.buscaPorId(Mockito.anyLong()))
				.willReturn(this.obterDadosLancamento());
		
		mvc.perform(MockMvcRequestBuilders
				.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	private String obterJsonRequestPost() throws JsonProcessingException {
		LancamentoDTO lancamentoDto = new LancamentoDTO();
		lancamentoDto.setId(null);
		lancamentoDto.setData(this.dateFormat.format(DATA));
		lancamentoDto.setTipo(TIPO);
		lancamentoDto.setIdFuncionario(ID_FUNCIONARIO);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(lancamentoDto);
	}
	
	private Response<LancamentoDTO> obterDadosLancamento() {
		Response<LancamentoDTO> response = new Response<LancamentoDTO>();
		LancamentoDTO lancamentoDto = new LancamentoDTO();
		lancamentoDto.setId(Optional.of(ID_LANCAMENTO));
		lancamentoDto.setData(this.dateFormat.format(DATA));
		lancamentoDto.setTipo(TipoEnum.valueOf(TIPO).toString());
		lancamentoDto.setIdFuncionario(ID_FUNCIONARIO);
		response.setData(lancamentoDto);
		return response;
	}
	
	private Response<FuncionarioDTO> obterDadosFuncionario() {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		FuncionarioDTO funcionarioDto = new FuncionarioDTO();
		funcionarioDto.setId(ID_FUNCIONARIO);
		response.setData(funcionarioDto);
		return response;
	}
	
	private Response<FuncionarioDTO> funcionarioInvalido() {
		Response<FuncionarioDTO> response = new Response<FuncionarioDTO>();
		response.getErrors().add("Funcionário não encontrado. ID inexistente.");
		return response;
	}
}
