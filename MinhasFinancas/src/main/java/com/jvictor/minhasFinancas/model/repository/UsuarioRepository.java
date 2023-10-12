package com.jvictor.minhasFinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jvictor.minhasFinancas.model.entity.Usuario;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	
	public boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	
	

}
