package com.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.pontointeligente.api.entities.Empresa;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {
	
	@Autowired
	private EmpresaRepository empresaRepo;
	
	private static final String CNPJ = "51463645000100";
	
	@Before
	public void seUp() throws Exception {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj(CNPJ);
		this.empresaRepo.save(empresa);
	}
	
	@After
	public final void tearDown() {
		this.empresaRepo.deleteAll();
	}
	
	@Test
	public void testBuscarPorCnpj() {
		Empresa empresa = this.empresaRepo.findByCnpj(CNPJ);
		assertEquals(CNPJ, empresa.getCnpj());
	}

}
