package com.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pontointeligente.api.entities.Empresa;
import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.entities.Lancamento;
import com.pontointeligente.api.enums.PerfilEnum;
import com.pontointeligente.api.enums.TipoEnum;
import com.pontointeligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	private FuncionarioRepository funcionarioRepo;
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	@Autowired
	private LancamentoRepository lancamentoRepo;
	
	private Long idFuncionario;
	
	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepo.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepo.save(obterDadosFuncionario(empresa));
		this.idFuncionario = funcionario.getId();
		
		this.lancamentoRepo.save(obterDadosLancamento(funcionario));
		this.lancamentoRepo.save(obterDadosLancamento(funcionario));
	}
	
	@After
	public final void tearDown() {
		this.empresaRepo.deleteAll();
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lancamentoRepo.findByFuncionarioId(idFuncionario);
		assertEquals(2, lancamentos.size());
	}
	
	@Test
	public void testeBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepo.findByFuncionarioId(idFuncionario, page);
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	private Lancamento obterDadosLancamento(Funcionario funcionario) throws NoSuchAlgorithmException {
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		return lancamento;
	}
	
	private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Fulano de tal");
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf("24291173474");
		funcionario.setEmail("email@valido.com");
		funcionario.setEmpresa(empresa);
		return funcionario;
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
}
