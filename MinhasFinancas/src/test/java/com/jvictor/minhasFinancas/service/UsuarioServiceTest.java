package com.jvictor.minhasFinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jvictor.minhasFinancas.exception.ErroAutenticacao;
import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Usuario;
import com.jvictor.minhasFinancas.model.repository.UsuarioRepository;
import com.jvictor.minhasFinancas.service.impl.UsuarioServiceImpl;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {
	
	
	 
	
	 @MockBean
	 UsuarioRepository repository;
	
	 @SpyBean
	 UsuarioServiceImpl service;

	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//varificacao
		Assertions.assertThat(result).isNotNull();
		
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmalInformado() {
		
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Ususario nao encontrado");
		
		
	}
	
	@Test
	public void deveLancarErroQuandoAsenhaNaoBater() {
		
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha Invalida");
		
	}
	
	
	
	
	
	

	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//acao
		service.validarEmail("email@email.com");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailCadastrado() {
		
		//cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		//acao
		service.validarEmail("email@email.com");
		
		
	}
	
	@Test
	public void  deveSalvarUmUsuario() {
		
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1L)
										   .nome("nome")
										   .email("email@email.com")
										   .senha("senha").build();		
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(usuario);
		
		//verificacao
		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	
	public void naoDeveSalvarUmUsuarioComEmailCadastrado() {
		
		String email = "email@email.com";
		
		//cenario
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//acao
		service.salvarUsuario(usuario);
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
		
		
	}
	
	
	
	
}
