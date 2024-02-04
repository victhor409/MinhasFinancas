package com.jvictor.minhasFinancas.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Lancamento;
import com.jvictor.minhasFinancas.model.enums.StatusLancamento;
import com.jvictor.minhasFinancas.model.enums.TipoLancamento;
import com.jvictor.minhasFinancas.model.repository.LancamentoRepository;
import com.jvictor.minhasFinancas.service.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private LancamentoRepository repository;

	public LancamentoServiceImpl(LancamentoRepository repository) {

		this.repository = repository;

	}

	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {

		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {

		Objects.requireNonNull(lancamento.getId());

		return repository.save(lancamento);
	}

	@Override
	public void deletar(Lancamento lancamento) {

		Objects.requireNonNull(lancamento.getId());

		repository.delete(lancamento);

	}

	@Override
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {

		Example example = Example.of(lancamentoFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);

	}

	@Override
	public void validar(Lancamento lancamento) {

		if (lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descricao");
		}

		if (lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um mes valido");
		}

		if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um ano valido");
		}

		if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Informe um usuario valido");
		}

		if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um valor valido");
		}

		if (lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo valido");
		}

	}

	@Override
	public Optional<Lancamento> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public BigDecimal obterSaldoPorTipoLancamentoEUsuario(Long id) {
		BigDecimal receitas = repository.obterSaldoPorTipoLancamento(id, TipoLancamento.RECEITA);
		BigDecimal despesa = repository.obterSaldoPorTipoLancamento(id, TipoLancamento.DESPESA);

		if (receitas == null) {
			receitas = BigDecimal.ZERO;
		}

		if (despesa == null) {
			despesa = BigDecimal.ZERO;
		}

		return receitas.subtract(despesa);
	}

}
