package com.jvictor.minhasFinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jvictor.minhasFinancas.exception.ErroAutenticacao;
import com.jvictor.minhasFinancas.exception.RegraNegocioException;
import com.jvictor.minhasFinancas.model.entity.Usuario;
import com.jvictor.minhasFinancas.model.repository.UsuarioRepository;
import com.jvictor.minhasFinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {

		Optional<Usuario> usuario = repository.findByEmail(email);

		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Ususario nao encontrado");
		}

		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha Invalida");
		}

		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {

		validarEmail(usuario.getEmail());

		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {

		boolean exists = repository.existsByEmail(email);

		if (exists) {
			throw new RegraNegocioException("Já Exste um Usuario com esse email cadastrado !");
		}
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {

		return repository.findById(id);
	}
}
