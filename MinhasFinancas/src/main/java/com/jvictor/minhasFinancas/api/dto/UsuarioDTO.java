package com.jvictor.minhasFinancas.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class UsuarioDTO {
	
	private String email;
	private String nome;
	private String senha;

}
