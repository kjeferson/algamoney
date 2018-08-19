package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager; // recebe as autenticações definidas no método configure do ResourceServerConfig

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")// usuario do cliente
				.secret("@ngul@r0") // senha do cliente
				.scopes("read","write") // permissões
				.authorizedGrantTypes("password","refresh_token") // implementação oauth do tipo password flux, e refresh token para implementar o refresh com acces token
				.accessTokenValiditySeconds(1800) //define quantos segundos o token vai permanecer ativo, ex: 1800seg ou 30min 
				.refreshTokenValiditySeconds(3600 * 24) // define validade do refresh para 1 dia
			.and()
				.withClient("mobile")// usuario do cliente
				.secret("m0b1l30") // senha do cliente
				.scopes("read") // permissões
				.authorizedGrantTypes("password","refresh_token") // implementação oauth do tipo password flux, e refresh token para implementar o refresh com acces token
				.accessTokenValiditySeconds(1800) //define quantos segundos o token vai permanecer ativo, ex: 1800seg ou 30min 
				.refreshTokenValiditySeconds(3600 * 24); // define validade do refresh para 1 dia
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints
			.tokenStore(tokenStore()) //define onde o token será guardado
			.accessTokenConverter(accessTokenConverter())
			.reuseRefreshTokens(false)//enquanto o refresh_token não expirar eu posso usar o access token
			.authenticationManager(authenticationManager); // valida a autenticação
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter(); 
		accessTokenConverter.setSigningKey("algaworks");// senha que valida o token
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		//return new InMemoryTokenStore(); // token gravado em memória
		return new JwtTokenStore(accessTokenConverter());
	}
	
}