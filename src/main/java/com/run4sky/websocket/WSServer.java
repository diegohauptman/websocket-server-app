package com.run4sky.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.google.gson.JsonObject;
import com.run4sky.beans.SecureDisp;
import com.run4sky.entities.Device;
import com.run4sky.json.JsonDecoder;
import com.run4sky.json.JsonEncoder;
import com.run4sky.network.GetPublicIP;
import com.run4sky.queries.DBQuery;

/**
 * Clase del Websocket Enpoint Server. Aqui se gerencia el ciclo de vida del
 * WebSocket por métodos anotados. Se crea un objeto de la clase
 * DeviceSessionHandler que contiene los métodos necesarios para guardar y
 * identificar cada sesion y su conexion correspondiente.
 * 
 * En el parámetro de ruta(@PathParam)
 * de @ServerEndpoint("/endpoint/{connection-type}") el device-type es el tipo
 * de dispositivo que el clinete enviará al servidor al iniciar la sesion
 * Websocket. Estos tipos de conexion pueden ser:
 * 
 * -internal: dentro de 0.run -external: -clientes: subnube -managers:
 * dispositivos especiales -"": dispositivo aun no registrado.
 * 
 * @author Diego
 */
@ServerEndpoint(value = "/endpoint/{connection-type}", encoders = { JsonEncoder.class }, decoders = {
		JsonDecoder.class }, configurator = ServerConfigurator.class)
public class WSServer {

	//FIXME private ServerEndpointConfig endpointConfig;
	
	private Session session;
	// Clase singleton que gestiona las sesiones.
	private DeviceSessionHandler sessionHandler = DeviceSessionHandler.getInstance();
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * M�todo que se ejecuta justo al iniciar la conexion con el Websocket, antes de
	 * cualquier otra cosa.
	 * 
	 * Por parámetros recibimos la session y la String "connection-type" para
	 * añadirlos al Hashmap correspondiente segun el tipo de conexion.
	 * 
	 * @param session
	 * @param connectionType
	 */
	@OnOpen
	public void onOpen(EndpointConfig config, Session session, @PathParam("connection-type") String connectionType) {
		
		//FIXME this.endpointConfig = (ServerEndpointConfig) config;
		//FIXME ServerConfigurator configurator = (ServerConfigurator) endpointConfig.getConfigurator();
		
		System.out.println("Ip Externo: " + GetPublicIP.getPublicIP(session).toString());
		this.session = session;
		System.out.println("Session: " + session.getId() + "\n Conexion: " + connectionType);
		session.getUserProperties().put(connectionType, session);
		sessionHandler.addSession(connectionType, session);
	}

	/**
	 * Metodo que se ejecuta al recibir mensages del cliente (recibe en String y se
	 * converte a Json) al que el servidor contesta a la sesion correcta en este
	 * mismo metodo enviando tambien objetos Json.
	 * 
	 * @param message
	 * @param session
	 * @return
	 * @return
	 */
	@OnMessage
	public JsonObject onMessage(JsonObject jsonMessage, Session session,
			@PathParam("connection-type") String connectionType) {

		this.session = session;
		System.out.println("Mensage Json cliente: " + jsonMessage.toString());
		
		String protocol = jsonMessage.get("protocol").getAsString();
		
		switch (protocol) {
		case "100":
			prot100(jsonMessage);
			logger.info("dentro del protocolo 100");
			break;

		default:
			logger.info("default del switch");
			break;
		}
		
		

		// envia un mensage de texto al cliente
		// this.sendTextMessage("Hola, soy el servidor y he recebido su mensaje: " +
		// message + "// tienes la sesión: " + session.getId());

		return jsonMessage;

	}

	/**
	 * Cuando el cliente cierra la conexion.
	 */
	@OnClose
	public void onClose(Session session, @PathParam("connection-type") String connectionType, CloseReason closeReason) {

		System.out.println("Session: " + session.getId() + " cerrando...");
		sessionHandler.removeSession(session, connectionType);

		if (!(closeReason.getCloseCode().equals(CloseCodes.GOING_AWAY)
				|| closeReason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE)))
			logger.info(String.format("Sesion %s no ha cerrado normalmente porque %s", session.getId(), closeReason));
	}

	/**
	 * Cuando hay algun error.
	 * 
	 * @param t
	 */
	@OnError
	public void onError(Throwable t) {
		System.out.println("dentro de onError");
		logger.severe(t.getMessage());

	}

	private void sendTextMessage(String message) {
		try {
			this.session.getBasicRemote().sendText(message);
		} catch (IOException ioe) {
			System.out.println("Error al enviar mensaje " + ioe.getMessage());
		}
	}

	private void sendJsonMessage() {

		// JsonObject de Google Gson
		JsonObject gsonObject = new JsonObject();
		gsonObject.addProperty("Message", "Hola, soy el servidor y te envio tus datos");
		gsonObject.addProperty("Session", this.session.getId());
		gsonObject.addProperty("public IP", GetPublicIP.getPublicIP(session).toString());
		System.out.println("Gson message: " + gsonObject.toString());

		try {
			this.session.getBasicRemote().sendObject(gsonObject);
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	private SecureDisp prot100(JsonObject jsonMessage) {
		
		String mac = jsonMessage.get("mac").getAsString();
		
		//FIXME estas queries hay que cambiarlas para recibir parametros (???) (como se hace en Hibernate?)
		String sqlSecureDisp = "FROM SecureDisp WHERE mac = " + mac;
		String sqlInsernalService = "FROM InsernalService WHERE mac = " + mac;
		
		//Lista que recibe dispositivos que tiene esta Mac. En teoria tiene que ser uno solo.
		List dispList = DBQuery.secureDispList(sqlSecureDisp);
		
		SecureDisp disp = new SecureDisp();
		
		if(dispList.size() == 1) {
			for (Iterator<SecureDisp> iterator = dispList.iterator(); iterator.hasNext();){
				disp = iterator.next(); 
				System.out.println("ID: "+disp.getId());
				
				return disp;
	         }
		}
		else if(dispList.size() > 1) {
			logger.warning("M�s de un dispositivo con esta Mac!");
			for (Iterator<SecureDisp> iterator = dispList.iterator(); iterator.hasNext();) {
				disp = iterator.next(); 
		        System.out.println("ID: "+disp.getId());
			}
			
		}
		return null;
	}
	
}
