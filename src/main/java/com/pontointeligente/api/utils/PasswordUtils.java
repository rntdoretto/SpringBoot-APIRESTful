package com.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
	
	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);
	
	public PasswordUtils() {
	}

	/**
	 * Gera um hash utilizando o BCryt
	 * @param senha
	 * @return String
	 */
	public static String gerarBCrypt(String senha) {
		if (senha == null)
			return senha;
		log.info("Gerando hash com o BCrypt.");
		BCryptPasswordEncoder bcEncoder = new BCryptPasswordEncoder();
		return bcEncoder.encode(senha);
	}

	/**
	 * Verifica se senha é válida.
	 * @param senha
	 * @param senhaEncoded
	 * @return boolean
	 */
	public static boolean senhaValida(String senha, String senhaEncoded) {
		BCryptPasswordEncoder bcEncoder = new BCryptPasswordEncoder();
		return bcEncoder.matches(senha, senhaEncoded);
	}
}
