package com.example.algamoney.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * Exemplo de como encodar uma senha com BCryptPasswordEncoder
 * @author Jeferson
 *
 */
public class GeradorSenha {
    public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("@ngul@r0"));
	}
}
