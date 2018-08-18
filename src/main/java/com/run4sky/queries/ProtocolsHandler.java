package com.run4sky.queries;

import java.util.ArrayList;
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
 * 
 * @author user
 *
 */
public class ProtocolsHandler {

	private static Logger logger = Logger.getLogger("com.run4sky.queries.ProtocolsHandler");
	private static SessionFactory factory;

	/**
	 * Metodo que gestiona el protocolo 100. Busca la direccion Mac del dispositivo
	 * en las tablas. Este metodo devuelve una lista de cualquier clase/objeto segun
	 * el tipo de dispositivo encontrado. Si no encuentra el dispositivo devuelve
	 * una lista con una String con el mensaje "Dispositivo no encontrado".
	 * 
	 * @param <T>
	 * 
	 */
	public static <T> List<T> prot100(JsonObject jsonMessage) {

		String mac = jsonMessage.get("mac").getAsString();
		logger.info("Mac: " + mac);
		//Array con las clases que serán buscadas.
		Class[] classes = {InsernalService.class, SecureDisp.class, ExternalDisp.class, ClienService.class};
		//Lista con el mensage de que el dispositivo no ha sido encontrado.
		List<T> notFoundList = new ArrayList<>();
		String deviceNotFound = "Dispositivo no encontrado";
		notFoundList.add((T) deviceNotFound);

		GenericDAO dao = new GenericDAO();
		
		for (Class clazz : classes) {
			List<T> list = dao.findByProperty(clazz, "mac", mac);
			if(!list.isEmpty()) {
				return list;
			} 
		}
		
		logger.info("Dispositivo no encontrado");
		return notFoundList;
		 
	}

}
