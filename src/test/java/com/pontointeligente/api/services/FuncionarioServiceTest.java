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

import com.pontointeligente.api.dtos.FuncionarioDTO;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.repositories.FuncionarioRepository;
import com.pontointeligente.api.response.Response;
import com.pontointeligente.api.services.impl.FuncionarioServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {

	@MockBean
	private FuncionarioRepository funcionarioRepo;
	
	@Autowired
	private FuncionarioServiceImpl funcionarioService;
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.funcionarioRepo.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
	}
	
	@Test
	public void testPersistirFuncionario() {
		Response<FuncionarioDTO> response = this.funcionarioService.persistir(new FuncionarioDTO());
		assertNotNull(response.getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorId() {
		BDDMockito.given(this.funcionarioRepo.findOne(Mockito.anyLong())).willReturn(new Funcionario());
		Response<FuncionarioDTO> response = this.funcionarioService.buscarPorId(1L);
		assertNotNull(response.getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		BDDMockito.given(this.funcionarioRepo.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		Response<FuncionarioDTO> response = this.funcionarioService.buscarPorEmail("email@valido.com");
		assertNotNull(response.getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		BDDMockito.given(this.funcionarioRepo.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		Response<FuncionarioDTO> response = this.funcionarioService.buscarPorCpf("24291173474");
		assertNotNull(response.getData());
	}
}
