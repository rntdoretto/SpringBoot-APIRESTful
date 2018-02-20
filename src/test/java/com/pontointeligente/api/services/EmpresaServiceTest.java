package com.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pontointeligente.api.dtos.EmpresaDTO;
import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.repositories.EmpresaRepository;
import com.pontointeligente.api.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {

	@MockBean
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.empresaRepository.save(Mockito.any(Empresa.class)))
				.willReturn(new Empresa());
		BDDMockito.given(this.empresaRepository.findOne(Mockito.anyLong()))
				.willReturn(new Empresa());
		
	}
	
	@Test
	public void testBuscarEmpresaPorCnpj() {
		Response<EmpresaDTO> response = this.empresaService.buscarPorId(1L);
		assertNotNull(response.getData());
	}
	
	@Test
	public void testPersistirEmpresa() {
		Response<EmpresaDTO> response = this.empresaService.persistir(new EmpresaDTO());
		assertNotNull(response.getData());
	}
}
