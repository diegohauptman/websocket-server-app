package com.run4sky.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.EncodeException;
import javax.websocket.Session;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Clase que gestiona y guarda en HashMaps las sesiones con sus correspondientes
 * dispositivos. 
 *
 * @author user
 */
public class SessionHandler {
	
	//Map de sesiones. 
	//Key - String connectionType
	//Value - List segun el tipo de dispositivos conectados.
    private static final Map<String, List<Session>> sessionsMap = new HashMap<>();
    
    //Listas que guardan las sesiones segun el dispositivo
    private static final List<Session> internalDevicesSessionList = new ArrayList<>();
    private static final List<Session> externalDevicesSessionList = new ArrayList<>();
    private static final List<Session> managersSessionList = new ArrayList<>();
    private static final List<Session> clientSessionList = new ArrayList<>();
    private static final List<Session> unsignedSessionList = new ArrayList<>();
    
    //Instancia estatica de la clase para crear un singleton
    private static SessionHandler instance;

    private SessionHandler() {
    }
    
    //Constructor estatico que devuelve una unica instancia estatica de la clase: Singleton
    public static SessionHandler getInstance() {
        if (instance == null) {
            instance = new SessionHandler();
        }

        return instance;
    }
    

    public static Map<String, List<Session>> getSessionsmap() {
		return sessionsMap;
	}

	/**
     * Se anade las sesiones en HashMaps para identificar cual sesion
     * corresponde a que tipo de conexion.
     *
     * @param connectionType
     * @param session
     */
    public void addSession(String connectionType, Session session) {

        
        System.out.println("Entra en el metodo addSession.");

        if (session.getUserProperties().containsKey("com.run4sky.beans.InsernalService")) {
            System.out.println("InternalService");
            internalDevicesSessionList.add(session);
            sessionsMap.put(connectionType, internalDevicesSessionList);
            printSessionMap();
        }
        if (session.getUserProperties().containsKey("com.run4sky.beans.ExternalDisp")) {
            System.out.println("ExternalDisp");
            externalDevicesSessionList.add(session);
            sessionsMap.put(connectionType, externalDevicesSessionList);
            printSessionMap();
        }
        if (session.getUserProperties().containsKey("com.run4sky.beans.SecureDisp")) {
            System.out.println("SecureDisp");
            managersSessionList.add(session);
            sessionsMap.put(connectionType, managersSessionList);
            printSessionMap();
        }
        if (session.getUserProperties().containsKey("com.run4sky.beans.ClienService")) {
            System.out.println("ClienService");
            clientSessionList.add(session);
            sessionsMap.put(connectionType, clientSessionList);
            printSessionMap();
        }
        if (session.getUserProperties().containsKey("java.lang.String")) {
            System.out.println("Conexion no registrada!");
            unsignedSessionList.add(session);
            sessionsMap.put(connectionType, unsignedSessionList);
            printSessionMap();
        }

    }
    
    //Imprime en consola las sesiones.
    private void printSessionMap() {
        for (Map.Entry<String, List<Session>> entry : sessionsMap.entrySet()) {
            String connection = entry.getKey();
            List<Session> sessions = entry.getValue();
            for (Session session1 : sessions) {
                System.out.println("\nDevice: " + connection + "\nSession:" + session1.getId());
            }
        }
    }

    /**
     *
    
     
    public void sendToSession(String message, Session session, String deviceID) {

        try {
            //creamos un objeto Json que recibirá el mensage.
            JSONObject json = new JSONObject(message);
            if (deviceID.equals("")) {
                for (Map.Entry<String, List<Session>> entry : sessionsMap.entrySet()) {
                    String macAdresss = entry.getKey();
                    List<Session> sessions = entry.getValue();
                    for (Session session1 : sessions) {
                        if (session.equals(session1)) {
                            session.getBasicRemote().sendObject(json);
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (IOException | EncodeException ex) {
            ex.printStackTrace();
        }
    }
    
    */
    /**
     * Quita la sesion de la lista de sesiones. Metodo usado en onClose.
     * @param session
     * @param connectionType
     */
    public void removeSession(Session session, String connectionType) {
    	
    	List<Session> sessionsList = Collections.synchronizedList(new ArrayList<>());
    	String key;
    	Session leavingSession = null;
    	//Muestra las sesiones en la lista antes de cerrarlas
        for (Map.Entry<String, List<Session>> entry : sessionsMap.entrySet()) {
            key = entry.getKey();
            if (key.equals(connectionType)) {
                sessionsList = sessionsMap.get(key);
                for (Session session1 : sessionsList) {
                    System.out.println("SessionList before removal: " + session1.getId());
                }
            }
        }
        //Guarda las sessiones salientes en una variable de tipo Session para imprimir en consola.
        for (Session session1 : sessionsList) {
            if (session1.getId().equals(session.getId())) {
            	leavingSession = session;
            }
        }
        //Quita la sesion de la lista.
        sessionsList.remove(session);
        //Imprime en consola la sesion saliente.
        System.out.println("Removed session " + leavingSession.getId());
        //Imprime en consola la lista con las sesiones que quedaron.
        for (Session session2 : sessionsList) {
            System.out.println("SessionList after removal: " + session2.getId());
        }
    }
}

