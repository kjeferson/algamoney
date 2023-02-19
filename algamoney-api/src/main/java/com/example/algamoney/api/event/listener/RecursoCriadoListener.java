package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.example.algamoney.api.event.RecursoCriadoEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//Listener de aplicação de evento
@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {
    @Override
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {
		HttpServletResponse response = recursoCriadoEvent.getResponse();
		Long codigo = recursoCriadoEvent.getCodigo();
		
		adicionarHeaderLocation(response, codigo);
	}

	private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		//URI para recuperar o recurso criado para retornar na Header
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(codigo).toUri();
		//Setar a Header Location com a uri
		response.setHeader("Location", uri.toASCIIString());
	}
}
