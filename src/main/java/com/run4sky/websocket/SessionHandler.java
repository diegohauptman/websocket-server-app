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
 * dispositivos. Hay una HashMap para cada tipo de conexion:
 * -internalDevicesSessionList: en 0.run4sky -externalDevicesSessionList:
 * -managersSessionId -clientDevices -unsigned
 *
 * @author user
 */
public class SessionHandler {

    private static final Map<String, List<Session>> sessionsMap = new HashMap<>();

    private static final List<Session> internalDevicesSessionList = new ArrayList<>();
    private static final List<Session> externalDevicesSessionList = new ArrayList<>();
    private static final List<Session> managersSessionList = new ArrayList<>();
    private static final List<Session> clientSessionList = new ArrayList<>();
    private static final List<Session> unsignedSessionList = new ArrayList<>();

    private static SessionHandler instance;

    private SessionHandler() {
    }

    public static SessionHandler getInstance() {
        if (instance == null) {
            instance = new SessionHandler();
        }

        return instance;
    }

    /**
     * Se añade las sesiones en HashMaps para identificar cual sesion
     * corresponde a que tipo de conexion.
     *
     * @param connectionType
     * @param session
     */
    public void addSession(String connectionType, Session session) {

        //Ejemplo de lo que deve hacer addSession
        //Cada sesion viene con un parametro (@PathParam) se se guarda como 
        //clave en un Map implementado en Websocket API (UserProperties).
        //Buscamos por getUserProperties() por la clave y obtenemos como valor 
        //la sesion correspondiente. 
        //Si el dispositivo no esta registrado, el ID será la MacAddress
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
     * @param message
     * @param session
     * @param deviceID
     */
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

    public void removeSession(Session session, String connectionType) {
    	
    	List<Session> sessionsList = Collections.synchronizedList(new ArrayList<>());
    	String key;
    	Session leavingSession = null;
    	
        for (Map.Entry<String, List<Session>> entry : sessionsMap.entrySet()) {
            key = entry.getKey();
            if (key.equals(connectionType)) {
                sessionsList = sessionsMap.get(key);
                for (Session session1 : sessionsList) {
                    System.out.println("SessionList before removal: " + session1.getId());
                }
            }
        }
        
        for (Session session1 : sessionsList) {
            //System.out.println("SessionList: " + session1.getId());
            if (session1.getId().equals(session.getId())) {
            	leavingSession = session;
            }
        }
        
        sessionsList.remove(session);
        System.out.println("Removed session " + leavingSession.getId());
        for (Session session2 : sessionsList) {
            System.out.println("SessionList after removal: " + session2.getId());
        }
    }
}

