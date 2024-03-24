package com.jvictor.minhasFinancas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hibernate.criterion.Example;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Lancamento;
import com.jvictor.minhasFinancas.model.entity.Usuario;
import com.jvictor.minhasFinancas.model.enums.StatusLancamento;
import com.jvictor.minhasFinancas.model.repository.LancamentoRepository;
import com.jvictor.minhasFinancas.model.repository.LancamentoRepositoryTest;
import com.jvictor.minhasFinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class LancamentoServiceTeste {

	@MockBean
	LancamentoRepository repository;// mock sempre do repository

	@SpyBean
	LancamentoServiceImpl service;// spi da classe de estamos testando

	@Test
	public void deveSalvarUmLancamentoComSucesso() {

		// cenario
		Lancamento lancamentoASalvo = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvo);

		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvo)).thenReturn(lancamentoSalvo);

		// =========================================================================="
		// execucao
		Lancamento lancamento = service.salvar(lancamentoASalvo);

		// verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}

	@Test
	public void naoDevaSalvarUmLancamentoQuandoHouverErroDeValidacao() {

		Lancamento lancamentoASalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoASalvo.setId(1l);
		lancamentoASalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvo);

		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvo), RegraNegocioException.class);
		
	}

	@Test
	public void deveAtualizarComSucesso() {

		// cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);

		// =========================================================================="
		// execucao
		 service.atualizar(lancamentoSalvo);

		// verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	}

	@Test
	public void deveLancarErroAoAtualizarLancamentoSemId() {
		
		//cenario
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		//verificacao
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoSalvo), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoSalvo);
	}
	
	public void deveDeletarUmLancamento() {
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		service.deletar(lancamento);
		
		Mockito.verify(repository).delete(lancamento);
		
	}
	
	
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoFalso() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		
		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
		
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = new ArrayList<>();
		lista.add(lancamento);
		Mockito.when(repository.findAll(Mockito.any(org.springframework.data.domain.Example.class))).thenReturn(lista);
		
		//execucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacao
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarUmStatusDeUmLancamento() {
		
		//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execucao
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificacoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExistir() {
		
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}
	
//	@Test
//	public void deveLancarErrosAoValidarUmLancamento() {
//		Lancamento lancamento = new Lancamento();
//		
//		Throwable erro  =  Assertions.catchThrowable( () -> service.validar(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descricao");
//		
//		lancamento.setDescricao("");
//		
//		erro  =  Assertions.catchThrowable( () -> service.validar(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descricao");
//		
//		lancamento.setDescricao("salario");
//		
//		erro =  Assertions.catchThrowable( () -> service.validar(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		lancamento.setMes(0);
//		
//		erro =  Assertions.catchThrowable( () -> service.validar(lancamento));
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		
//		lancamento.setMes(13);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		lancamento.setMes(1);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		lancamento.setAno(202);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		lancamento.setAno(2000);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mes valido");
//		
//		lancamento.setUsuario(new Usuario());
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe Um mes valido");
//		
//		lancamento.getUsuario().setId(1l);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido");
//		
//		lancamento.setValor(BigDecimal.ZERO);
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido");
//		
//		lancamento.setValor(BigDecimal.valueOf(1));
//		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor valido");
//		
//		
//		
//		
//	}
	
	
}
