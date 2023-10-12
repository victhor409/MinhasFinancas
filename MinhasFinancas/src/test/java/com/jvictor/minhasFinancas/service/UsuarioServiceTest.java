package com.jvictor.minhasFinancas.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Usuario;
import com.jvictor.minhasFinancas.model.repository.UsuarioRepository;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioServiceTest {
	
	@Autowired
	public UsuarioService service;
	
	@Autowired
	public UsuarioRepository repository;

	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
		//cenario
		repository.deleteAll();
		
		//acao
		service.validarEmail("email@email.com");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailCadastrado() {
		
		//cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		repository.save(usuario);
		
		//acao
		service.validarEmail("email@email.com");
		
		
	}
	
	
	
	
	
}
