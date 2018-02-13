package com.pontointeligente.api.security.services;

import java.util.Optional;

import com.pontointeligente.api.security.entities.Usuario;

public interface UsuarioService {

	/**
	 * Busca e retorna um usuário através do email.
	 * @param email
	 * @return
	 */
	Optional<Usuario> buscaPorEmail(String email);
}
