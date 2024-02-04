package com.jvictor.minhasFinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jvictor.minhasFinancas.model.entity.Lancamento;
import com.jvictor.minhasFinancas.model.enums.TipoLancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
	@Query(value="SELECT sum(l.valor) FROM Lancamento l join l.usuario u where u.id = :idUsuario"
			+ " and l.tipo = :tipo group by u")
	BigDecimal obterSaldoPorTipoLancamento(@Param("idUsuario") Long idUsuario, @Param("tipo") TipoLancamento tipo);

}
