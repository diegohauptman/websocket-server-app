package com.run4sky.websocket;


import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.run4sky.entities.Device;
import com.run4sky.json.JsonDecoder;
import com.run4sky.json.JsonEncoder;

/**
 * Clase del Websocket Enpoint Server. Aqui se gerencia el ciclo de vida 
 * del WebSocket por métodos anotados.
 * Se crea un objeto de la clase DeviceSessionHandler que contiene los métodos necesarios para 
 * guardar y identificar cada sesion y su conexion correspondiente.
 * 
 * En el parámetro de ruta(@PathParam) de @ServerEndpoint("/endpoint/{connection-type}") el device-type es
 * el tipo de dispositivo que el clinete enviará al servidor al iniciar la sesion Websocket.
 * Estos tipos de conexion pueden ser:
 * 
 * -internal: dentro de 0.run
 * -external: 
 * -clientes: subnube
 * -managers: dispositivos especiales
 * -"": dispositivo aun no registrado.
 * 
 * @author Diego
 */
@ServerEndpoint(value="/endpoint/{connection-type}", 
				encoders = {JsonEncoder.class},
				decoders = {JsonDecoder.class})
public class WSServer {
   private Session session;
   
    //Clase singleton que gestionar� las sesiones.
    private DeviceSessionHandler sessionHandler = DeviceSessionHandler.getInstance();
    
    /**
     * M�todo que se ejecuta justo al iniciar la conexion 
     * con el Websocket, antes de cualquier otra cosa.
     * 
     * Por parámetros recibimos la session y la String "connection-type" para añadirlos al Hashmap 
     * correspondiente segun el tipo de conexion.
     * @param session
     * @param connectionType
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("connection-type") String connectionType) {
    	this.session = session;
        System.out.println("Session: " + session.getId() + "\n Conexion: "+connectionType);
        session.getUserProperties().put(connectionType, session);
        sessionHandler.addSession(connectionType, session);
    }
    
    /**
     * Método que se ejecuta al recibir mensages del cliente (recibe en String y
     * se convertirá a Json) al que el servidor contestará a la sesión correcta 
     * en este mismo método enviando también objetos Json.
     * 
     * @param message
     * @param session
     * @return
     */
    @OnMessage
    public void onMessage(com.google.gson.JsonObject jsonMessage, Session session, @PathParam("connection-type") String connectionType) {
    	this.session = session;
        //Aqui se definen protocolos con switch case.
        
        
    	System.out.println("Mensage cliente: " + jsonMessage.toString());
    	
    	//crea un objeto Device 
        Device device = new Device();
        
        //get received Json data
//        JsonReader jsonReader = Json.createReader(new StringReader(message));
//        JsonObject jsonObject = jsonReader.readObject();
//        jsonReader.close();
        
        
        
        String mac = jsonMessage.get("mac").getAsString();
        String ip = jsonMessage.get("ip").getAsString();
        
        device.setMacAddress(mac);
        device.setIp(ip);
        
        System.out.println("Device mac: " + device.getMacAddress() + "\nDevice ip: " + device.getIp());
        
        
        //envia un mensage al cliente
        //this.sendTextMessage("Hola, soy el servidor y he recebido su mensaje: " + message + "// tienes la sesión: " + session.getId());
        this.sendJsonMessage();
        
        
       
         
    }

    /**
     *
     */
    @OnClose
    public void onClose(Session session, @PathParam("connection-type") String connectionType) {
        System.out.println("Session: " + session.getId()+ " cerrando...");
        sessionHandler.removeSession(session, connectionType);
        
    }

    /**
     *
     * @param t
     */
    @OnError
    public void onError(Throwable t) {
    	System.out.println("dentro de onError");
        System.out.println(t.getMessage());
        
    }
   
    private void sendTextMessage(String message) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException ioe) {
                System.out.println("Error al enviar mensaje " + ioe.getMessage());
            }
        }
    
    private void sendJsonMessage() {
        /*
        Solo un ejemplo de como se recibiria el mensaje del cliente, lo ponemos en un Json
        y lo enviaria de vuelta como respuesta en Json:
        */
    	
    	//JsonObject de Google Gson
    	com.google.gson.JsonObject gsonObject = new com.google.gson.JsonObject();
    	gsonObject.addProperty("GSON", "Hello GSON");
    	gsonObject.addProperty("Session", this.session.getId());
    	System.out.println("Gson message: " + gsonObject.toString());
    	

    	//JsonObject de libreria de glassfish JsonP
//    	JsonObject json = Json.createObjectBuilder()
//                .add("JSON", "Hello Json")
//                .add("Session", this.session.getId())
//                .build();
//    	System.out.println("Json message: " + json.toString());
    	
    	
    	//Json de json.org
//    	String jsonString = new JSONObject()
//    			.put("JSON", "Hello Json")
//    			.put("Session", this.session.getId()).toString();
//    	System.out.println("Json message: " + jsonString);
    	
        try {
			this.session.getBasicRemote().sendObject(gsonObject);
		} catch (IOException | EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

