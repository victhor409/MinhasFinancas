package com.jvictor.minhasFinancas.model.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Lancamento;
import com.jvictor.minhasFinancas.model.enums.StatusLancamento;
import com.jvictor.minhasFinancas.model.enums.TipoLancamento;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	public static LancamentoRepository repository;
	
	

	@Autowired
	TestEntityManager testEntityManager;

	@Test
	public void deveSalvarUmLancamento() {

		Lancamento lancamento = Lancamento.builder().ano(2019).mes(1).descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10)).tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now()).build();

		lancamento = repository.save(lancamento);

		Assertions.assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	public void deveDeveletarUmLancamento() {
		Lancamento lancamento = criarLancamento();
		testEntityManager.persist(lancamento);

		lancamento = testEntityManager.find(Lancamento.class, lancamento.getId());

		repository.delete(lancamento);

	}

	@Test
	public void deveBuscarLancamentoPorId() {
		Lancamento lancamento = criaEpersisteUmLancamento();

		Optional<Lancamento> findById = repository.findById(lancamento.getId());

		Assertions.assertThat(findById.isPresent()).isTrue();
	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criaEpersisteUmLancamento();

		lancamento.setAno(2020);
		lancamento.setDescricao("Teste Atualizado");
		lancamento.setStatus(StatusLancamento.CANCELADO);

		repository.save(lancamento);

		Lancamento lancamentoAtualizado = testEntityManager.find(Lancamento.class, lancamento.getId());

		Assertions.assertThat(lancamentoAtualizado.getAno()).isEqualTo(2020);
		Assertions.assertThat(lancamentoAtualizado.getDescricao()).isEqualTo("Teste Atualizado");
		Assertions.assertThat(lancamentoAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);

	}

	private Lancamento criaEpersisteUmLancamento() {
		Lancamento lancamento = criarLancamento();
		testEntityManager.persist(lancamento);
		return lancamento;
	}

	public static Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder().ano(2019).mes(1).descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10)).tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now()).build();

	
		return lancamento;
	}
	
	

}
