package com.run4sky.queries;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;

import com.google.gson.JsonObject;
import com.run4sky.beans.ClienService;
import com.run4sky.beans.ExternalDisp;
import com.run4sky.beans.InsernalService;
import com.run4sky.beans.SecureDisp;


/**
 * Clase que gestiona los protocolos.
 * @author user
 *
 */
public class ProtocolsHandler {
	
	private static Logger logger = Logger.getLogger("com.run4sky.queries.ProtocolsHandler");
	private static SessionFactory factory;
	
	/**
	 * Metodo que gestiona el protocolo 100. Busca la direccion Mac del dispositivo en las tablas.
	 * Este metodo devuelve una lista de cualquier clase/objeto segun el tipo de dispositivo encontrado.
	 * Si no encuentra el dispositivo devuelve null.
	 * @param <T>
	 * 
	 */
	public static <T> List<T> prot100(JsonObject jsonMessage) {
		
		String mac = jsonMessage.get("mac").getAsString();
		
		List<T> internalServiceList = null;
		List<T> secureDispList = null;
		List<T> externalDispList = null;
		List<T> clientService = null;
		
		GenericDAO dao = new GenericDAO();
		
		internalServiceList = (List<T>) dao.findByProperty(InsernalService.class, "mac", mac);
		if (!internalServiceList.isEmpty()) {
			System.out.println("en inernalService");
			return internalServiceList;
		}else if(internalServiceList.isEmpty()) {
			secureDispList = (List<T>) dao.findByProperty(SecureDisp.class, "mac", mac);
			if(!secureDispList.isEmpty()) {
				System.out.println("en secureDisp");
				return secureDispList;
			}
		}else if(secureDispList.isEmpty()) {
			externalDispList = (List<T>) dao.findByProperty(ExternalDisp.class, "mac", mac);
			if(!externalDispList.isEmpty()) {
				System.out.println("en externalDisp");
				return externalDispList;
			}
		}else if(externalDispList.isEmpty()) {
			clientService = (List<T>) dao.findByProperty(ClienService.class, "mac", mac);
			if(!clientService.isEmpty()) {
				System.out.println("en clientServiceList");
				return clientService;
			}
		}
		logger.info("Dispositivo no encontrado");
		return null;
		
		
		
		//Prueba que busca en todas las tablas y despues hace la evaluacion si ha encontrado el dispositivo o no.
		
		/*
		internalServiceList = (List<T>) dao.findByProperty(InsernalService.class, "mac", mac);
		secureDispList = (List<T>) dao.findByProperty(SecureDisp.class, "mac", mac);
		externalDispList = (List<T>) dao.findByProperty(ExternalDisp.class, "mac", mac);
		clientService = (List<T>) dao.findByProperty(ClienService.class, "mac", mac);
		
		
		if (!internalServiceList.isEmpty()) {
			System.out.println("en inernalService");
			return internalServiceList;
		}else if (!secureDispList.isEmpty()) {
			System.out.println("en secureDisp");
			return secureDispList;
		}else if (!externalDispList.isEmpty()) {
			System.out.println("en externalDisp");
			return externalDispList;
		}else if (!clientService.isEmpty()){
			System.out.println("en clientService");
			return clientService;
		} else {
			logger.info("Dispositivo no encontrado");
			return null;
		}
		
		*/
	}
	
	
	
}
