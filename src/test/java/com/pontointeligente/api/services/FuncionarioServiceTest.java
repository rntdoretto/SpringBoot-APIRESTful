package com.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

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
		BDDMockito.given(this.funcionarioRepo.findOne(Mockito.anyLong())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepo.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(this.funcionarioRepo.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
	}
	
	@Test
	public void testPersistirFuncionario() {
		ResponseEntity<Response<FuncionarioDTO>> responseEntity = this.funcionarioService.persistir(new FuncionarioDTO(), new BindingResult() {
			
			@Override
			public void setNestedPath(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void rejectValue(String arg0, String arg1, Object[] arg2, String arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void rejectValue(String arg0, String arg1, String arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void rejectValue(String arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reject(String arg0, Object[] arg1, String arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reject(String arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void reject(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void pushNestedPath(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void popNestedPath() throws IllegalStateException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean hasGlobalErrors() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasFieldErrors(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasFieldErrors() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean hasErrors() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String getObjectName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNestedPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<ObjectError> getGlobalErrors() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getGlobalErrorCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public ObjectError getGlobalError() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getFieldValue(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Class<?> getFieldType(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<FieldError> getFieldErrors(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<FieldError> getFieldErrors() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getFieldErrorCount(String arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getFieldErrorCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public FieldError getFieldError(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public FieldError getFieldError() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getErrorCount() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public List<ObjectError> getAllErrors() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void addAllErrors(Errors arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String[] resolveMessageCodes(String arg0, String arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] resolveMessageCodes(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void recordSuppressedField(String arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public Object getTarget() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] getSuppressedFields() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getRawFieldValue(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PropertyEditorRegistry getPropertyEditorRegistry() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, Object> getModel() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public PropertyEditor findEditor(String arg0, Class<?> arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void addError(ObjectError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		assertNotNull(responseEntity.getBody().getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorId() {
		ResponseEntity<Response<FuncionarioDTO>> responseEntity = this.funcionarioService.buscarPorId(1L);
		assertNotNull(responseEntity.getBody().getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorEmail() {
		ResponseEntity<Response<FuncionarioDTO>> responseEntity = this.funcionarioService.buscarPorEmail("email@valido.com");
		assertNotNull(responseEntity.getBody().getData());
	}
	
	@Test
	public void testBuscarFuncionarioPorCpf() {
		ResponseEntity<Response<FuncionarioDTO>> responseEntity = this.funcionarioService.buscarPorCpf("24291173474");
		assertNotNull(responseEntity.getBody().getData());
	}
}
