package com.example.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * Class para criar Filtro do Cors, adicionando apenas Headers
 * @author Jeferson
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) //Filtro com ordem de prioridade bem alta, para ser executado logo de inicio
public class CorsFilter implements Filter {

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;	

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        		
        HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		//Permitir Origin
		response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOriginPermitida());
		//Permitir que o Cookie do refresh token seja enviado
		response.setHeader("Access-Control-Allow-Credentials", "true");
		
		//Se originPermitida for igual a origin que veio do browser e for uma requisição OPTIONS, permitir
        //se não, não permitir e o Cors não vai estar habilitado
		if ("OPTIONS".equals(request.getMethod()) && algamoneyApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))) {
			//Permitir métodos HTTP: POST, GET, DELETE, PUT, OPTIONS
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			//Permitir os headers: Authorization, Content-Type, Accept
			response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			//Retorna Ok e para o filtro, sem precisar continuar com a requisição
			response.setHeader("Access-Control-Max-Age", "3600");
			
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(req, resp);
		}
        
    }
    
}
