package com.run4sky.websocket;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;
import com.run4sky.json.JsonDecoder;
import com.run4sky.json.JsonEncoder;
import com.run4sky.network.GetPublicIP;
import com.run4sky.queries.ProtocolsHandler;

/**
 * Clase del Websocket Enpoint Server. Aqui se gerencia el ciclo de vida del
 * WebSocket por métodos anotados. Se crea un objeto de la clase
 * SessionHandler que contiene los métodos necesarios para guardar y
 * identificar cada sesion y su conexion correspondiente.
 * 
 * @author Diego
 */
@ServerEndpoint(value = "/endpoint", encoders = { JsonEncoder.class }, decoders = {
		JsonDecoder.class }, configurator = ServerConfigurator.class)
public class WSServer {
	
	private Session session;
	private String deviceType;
	// Clase singleton que gestiona las sesiones.
	private SessionHandler sessionHandler = SessionHandler.getInstance();
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Metodo que se ejecuta justo al iniciar la conexion con el Websocket, antes de
	 * cualquier otra cosa.
	 * 
	 * Por parámetros recibimos la session y la String "connection-type" para
	 * añadirlos al Hashmap correspondiente segun el tipo de conexion.
	 * 
	 * @param session
	 * @param connectionType
	 */
	@OnOpen
	public void onOpen(EndpointConfig config, Session session) {

		System.out.println("Ip Externo: " + GetPublicIP.getPublicIP(session).toString());
		this.session = session;
		
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
	public void onMessage(JsonObject jsonMessage, Session session) {

		this.session = session;
		System.out.println("Mensage Json cliente: " + jsonMessage.toString());

		int protocol = jsonMessage.get("protocol").getAsInt();

		switch (protocol) {
		case 100:
			logger.info("dentro del protocolo 100");
			
			List<?> list = ProtocolsHandler.prot100(jsonMessage);
			deviceType = getDeviceType(list);
			logger.info("Session: " + session.getId() + "\n DeviceType: " + deviceType);
			System.out.println("Session: " + session.getId() + "\n DeviceType: " + deviceType);
			session.getUserProperties().put(deviceType, session);
			sessionHandler.addSession(deviceType, session);
			sendJsonMessage(deviceType);

			break;
		default:
			logger.info("default del switch");
			break;
		}
	}

	/**
	 * Cuando el cliente cierra la conexion.
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {

		System.out.println("Session: " + session.getId() + " cerrando...");
		sessionHandler.removeSession(session, deviceType);

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

	/**
	 * Construye un objeto Json e envia al cliente.
	 * 
	 * @param deviceType
	 */
	private void sendJsonMessage(String deviceType) {

		// JsonObject de Google Gson
		JsonObject gsonObject = new JsonObject();
		gsonObject.addProperty("Message", "Hola, soy el servidor y te envio tus datos");
		gsonObject.addProperty("Session", this.session.getId());
		gsonObject.addProperty("public IP", GetPublicIP.getPublicIP(session).toString());
		gsonObject.addProperty("ConnectionType", deviceType);
		System.out.println("Gson message: " + gsonObject.toString());

		try {
			this.session.getBasicRemote().sendObject(gsonObject);
		} catch (IOException | EncodeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que devuelve el tipo de disposivo en una lista.
	 * 
	 * @param list
	 * @return
	 */
	public String getDeviceType(List list) {
		Class<?> clazz = null;
		Object object = null;
		String objectClass = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			object = (Object) iterator.next();
			objectClass = object.getClass().getName();
			logger.info("Tipo de dispositivo: " + object.toString());
			System.out.println("Tipo de dispositivo: " + object.toString());
			clazz = object.getClass();
		}
		// Si el dispositivo ha sido encontrado (object != String) recoje su id y
		// imprime en consola.
		if (!(object instanceof String)) {
			try {
				Method getId = clazz.getMethod("getId");
				int id = (int) getId.invoke(object);
				logger.info("ID del dispositivo: " + id);
				System.out.println("ID del dispositivo: " + id);

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return objectClass;

	}
}
