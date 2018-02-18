package com.pontointeligente.api.security.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pontointeligente.api.entities.Funcionario;
import com.pontointeligente.api.repositories.FuncionarioRepository;
import com.pontointeligente.api.security.JwtUserFactory;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Funcionario> funcionario = Optional.ofNullable(funcionarioRepository.findByEmail(email));
		
		if (funcionario.isPresent()) {
			return JwtUserFactory.create(funcionario.get());
		}
		
		throw new UsernameNotFoundException("Email não encontrado.");
	}
}
