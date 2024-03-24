package com.jvictor.minhasFinancas.Controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jvictor.minhasFinancas.api.dto.UsuarioDTO;
import com.jvictor.minhasFinancas.model.entity.Usuario;
import com.jvictor.minhasFinancas.model.repository.LancamentoRepository;
import com.jvictor.minhasFinancas.service.LancamentoService;
import com.jvictor.minhasFinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private UsuarioService service;
	
	private LancamentoService lancamentoService;
	
	
	public UsuarioController(UsuarioService service, LancamentoService lancamentoService) {
		this.service = service;
		this.lancamentoService = lancamentoService;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
		
		
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto ) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e);
		}
	}
	
	
	
	@GetMapping("/saldo")
	public ResponseEntity obterSaldoPorUsuario( @PathVariable("id")  Long id) {
		
		  Optional<Usuario> usuario  =  service.obterPorId(id);
		
		  if(!usuario.isPresent()) {
			  return new ResponseEntity(HttpStatus.NOT_FOUND);
		  }
		
		BigDecimal saldo = lancamentoService.obterSaldoPorTipoLancamentoEUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
	
	
	
	
}
