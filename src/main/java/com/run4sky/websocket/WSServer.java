package com.run4sky.websocket;

import java.io.IOException;
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
	// Clase singleton que gestionar� las sesiones.
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
	 * Método que se ejecuta al recibir mensages del cliente (recibe en String y se
	 * convertirá a Json) al que el servidor contestará a la sesión correcta en este
	 * mismo método enviando también objetos Json.
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

		this.sendJsonMessage();

		// crea un objeto Device
		
		String mac = jsonMessage.get("mac").getAsString();
		String privateIp = jsonMessage.get("private ip").getAsString();
		String publicIp = jsonMessage.get("public ip").getAsString();
		String os = jsonMessage.get("os").getAsString();
		String cpuModel = jsonMessage.get("cpuModel").getAsString();
		int numberOfCPU = jsonMessage.get("numberOfCPU").getAsInt();
		long memoryQuantity = jsonMessage.get("memoryQuantity").getAsLong();
		
		

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
}
