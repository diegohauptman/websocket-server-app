package com.run4sky.websocket;

import java.net.URI;
import java.security.Principal;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
/**
 * Metodo que recoge los headers HTTP en el momento del upgrade al protocolo Websocket
 * y los imprime en consola.
 * @author mundakamacbook
 *
 */
public class ServerConfigurator extends ServerEndpointConfig.Configurator {
	
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		System.out.println("Handshake Request headers: " + request.getHeaders());
		System.out.println("Handshake Response headers: " + response.getHeaders());
		request.getHttpSession();
		URI requestURI = request.getRequestURI();
		System.out.println(requestURI.toString());
		//FIXME Principal userPrincipal = request.getUserPrincipal();
		

	}
}
