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
	private String deviceTypeString;
	// Clase singleton que gestiona las sesiones.
	private SessionHandler sessionHandler = SessionHandler.getInstance();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public Session getSession() {
		return this.session;
	}

	/**
	 * Metodo que se ejecuta justo al iniciar la conexion con el Websocket, antes de
	 * cualquier otra cosa.
	 * 
	 * @param session
	 * @param connectionType
	 */
	@OnOpen
	public void onOpen(EndpointConfig config, Session session) {

		System.out.println("Ip Externo: " + GetPublicIP.getPublicIP(session).toString());
		System.out.println("onOpen ->>> Session: " + session.getId());
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
		//protocolo 100 recibe las informaciones del dispositivo y busca en la base de dados si esta registrado.
		//segun el tipo de dispositivo, a�ade la sesion al HashMap del sessionHandler.
		//Envia un mensage al agente del dispositivo con la informacion encontrada (Si esta registado o no, cual tipo de dispositivo etc.)
		case 100:
			logger.info("\ndentro del switch case 100");
			
			List<?> list = Protocol.prot100(jsonMessage);
			Object deviceObject = getDeviceType(list);
			if(!isRegistered(deviceObject)) {
				Protocol.registerDevice(deviceObject, jsonMessage);
			}
			
			deviceTypeString = deviceObject.getClass().getName();
			logger.info("\nSession: " + session.getId() + "\nDeviceType: " + deviceTypeString);
			session.getUserProperties().put(deviceTypeString, session);
			sessionHandler.addSession(deviceTypeString, session);
			sendJsonMessage(deviceTypeString);

			break;
		default:
			logger.info("\ndefault del switch");
			break;
		}
	}

	/**
	 * Cuando el cliente cierra la conexion.
	 * 
	 * Quita las sessiones que se cierran del las listas de 
	 * sessiones en sessionHandler.removeSession(session, deviceTypeString).
	 *  
	 * Log del closeReason muestra si la sesion se ha cerrado normalmente.
	 */
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {

		System.out.println("Server onClose --> Session: " + session.getId() + " cerrando...");
		sessionHandler.removeSession(session, deviceTypeString);

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
	 * @param deviceTypeString
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
	public Object getDeviceType(List list) {
		Object object = null;
		Object objectClass = null;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			object = (Object) iterator.next();
			logger.info("\nTipo de dispositivo: " + object.toString());
			System.out.println("Tipo de dispositivo: " + object.toString());
		}
		// Si el dispositivo ha sido encontrado (object != String) recoje su id y
		// imprime en consola.
		if (!(object instanceof String)) {
			try {
				Method getId = object.getClass().getMethod("getId");
				int id = (int) getId.invoke(object);
				logger.info("\nID del dispositivo: " + id);

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		return object;

	}
	
	public boolean isRegistered(Object object) {
		
		boolean isRegistered = false;

		logger.info("Tipo de dispositivo: " + object.toString());
		System.out.println("Tipo de dispositivo: " + object.toString());
		try {
			Method getIsRegistered = object.getClass().getMethod("getIsRegistered", null);
			isRegistered = (boolean) getIsRegistered.invoke(object, null);
			logger.info("\nEl dispositivo está registrado? ---> " + isRegistered);

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		return isRegistered;

	}
}
