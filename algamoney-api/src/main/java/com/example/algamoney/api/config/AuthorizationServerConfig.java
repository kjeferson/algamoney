package com.example.algamoney.api.config;

import java.util.Arrays;

import com.example.algamoney.api.config.token.CustomTokenEnhancer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SuppressWarnings("deprecation")
@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
	private AuthenticationManager authenticationManager; // recebe as autenticações definidas no método configure do ResourceServerConfig

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;
    	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")// usuario do cliente
				.secret("$2a$10$UAc049fUm6Bxy8X/.mpn8.PfD2ncb4ZgvmEa5Hb.JOGVJNX1ampgG") // @ngul@r0 -> senha do cliente
				.scopes("read", "write") // permissões
				.authorizedGrantTypes("password", "refresh_token") // implementação oauth do tipo password flux, e refresh token para implementar o refresh com acces token
				.accessTokenValiditySeconds(1800) //define quantos segundos o token vai permanecer ativo, ex: 1800seg ou 30min 
				.refreshTokenValiditySeconds(3600 * 24) // define validade do refresh para 1 dia
			.and()
				.withClient("mobile")
				.secret(passwordEncoder.encode("m0b1le")) // Forma insegura -> usuario do cliente
				.scopes("read")//senha do cliente
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(1800)
				.refreshTokenValiditySeconds(3600 * 24);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//Token com mais detalhes
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		//Cadeia de objetos que trabalham com os tokens
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

		endpoints
			.authenticationManager(authenticationManager) // valida a autenticação
			.userDetailsService(userDetailsService)
			.tokenEnhancer(tokenEnhancerChain)
			.tokenStore(tokenStore())  //define onde o token será guardado
			.reuseRefreshTokens(false);//enquanto o refresh_token não expirar eu posso usar o access token
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();

		accessTokenConverter.setSigningKey("3032885ba9cd6621bcc4e7d6b6c35c2b");// senha que valida o token

		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		//return new InMemoryTokenStore(); // token gravado em memória
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}
}
