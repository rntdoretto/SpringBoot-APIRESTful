package com.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pontointeligente.api.dtos.LancamentoDTO;
import com.pontointeligente.api.entities.Lancamento;
import com.pontointeligente.api.repositories.LancamentoRepository;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@MockBean
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoServiceImpl lancamentoService;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
			.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class)))
				.willReturn(new Lancamento());
	}
	
	@Test
	public void testBuscarLancamentoPorIdFuncionario() {
		Response<Page<LancamentoDTO>> response = this.lancamentoService.buscarPorFuncionarioId(1L, 0, "id", "DESC");
		assertNotNull(response.getData());
	}
	
	@Test
	public void testBuscarLancamentoPorId() {
		BDDMockito.given(this.lancamentoRepository.findOne(Mockito.anyLong())).willReturn(new Lancamento());
		Response<LancamentoDTO> response = this.lancamentoService.buscaPorId(1L);
		assertNotNull(response.getData());
	}
	
	@Test
	public void testPersistirLancamento() {
		LancamentoDTO lancamentoDto = new LancamentoDTO();
		lancamentoDto.setData(this.dateFormat.format(new Date()));
		Response<LancamentoDTO> response = this.lancamentoService.persistir(lancamentoDto);
		assertNotNull(response.getData());
	}
}
