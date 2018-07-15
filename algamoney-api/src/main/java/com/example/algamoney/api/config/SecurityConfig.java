package com.example.algamoney.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration // anotação para indicar que a classe é de configuração
@EnableWebSecurity //esta anotação ja herda o @Configuration, não é necessário adicionar o @Configuration, fica como opcional
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("admin").password("admin").roles("ROLE");
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/categorias").permitAll() // todos tem permissão para acessar categorias
			.anyRequest().authenticated() // exceto categorias todos só podem acessar com autenticação
			.and()
		.httpBasic().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // para a aplicação não manter estado, não vai ter sessão nenhuma no servidor
		.csrf().disable(); // desabilita cross side request for j, para evitar script com injection para web, como não estamos usando web, não é necessário usar
	}
}
