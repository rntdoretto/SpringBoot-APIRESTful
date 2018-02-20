package com.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

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
import org.springframework.http.ResponseEntity;
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
	
//	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.lancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
			.willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
		BDDMockito.given(this.lancamentoRepository.findOne(Mockito.anyLong()))
				.willReturn(new Lancamento());
		BDDMockito.given(this.lancamentoRepository.save(Mockito.any(Lancamento.class)))
				.willReturn(new Lancamento());
	}
	
	@Test
	public void testBuscarLancamentoPorIdFuncionario() {
		ResponseEntity<Response<Page<LancamentoDTO>>> responseEntity = this.lancamentoService.buscarPorFuncionarioId(1L, 0, "id", "DESC");
		assertNotNull(responseEntity.getBody().getData());
	}
	
	/*@Test
	public void testBuscarLancamentoPorId() {
		ResponseEntity<Response<LancamentoDTO>> responseEntity = this.lancamentoService.buscaPorId(1L);
		assertNotNull(responseEntity.getBody().getData());
	}*/
	
	/*@Test
	public void testPersistirLancamento() {
		LancamentoDTO lancamentoDto = new LancamentoDTO();
		lancamentoDto.setData(this.dateFormat.format(new Date()));
		ResponseEntity<Response<LancamentoDTO>> responseEntity = this.lancamentoService.persistir(lancamentoDto, new BindingResult() {
			
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
	}*/
}
