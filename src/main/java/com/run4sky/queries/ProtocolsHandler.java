package com.run4sky.queries;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

	/**
	 * Metodo que gestiona el protocolo 100. Busca la direccion Mac del dispositivo
	 * en las tablas. Este metodo devuelve una lista de cualquier clase/objeto segun
	 * el tipo de dispositivo encontrado. Si no encuentra el dispositivo devuelve
	 * una lista con la String "Dispositivo no encontrado".
	 * 
	 * @param <T>
	 * 
	 */
	public static <T> List<T> prot100(JsonObject jsonMessage) {

		String mac = jsonMessage.get("mac").getAsString();
		logger.info("Mac: " + mac);
		
		//Array con las clases que seran buscadas.
		Class[] classes = {InsernalService.class, SecureDisp.class, ExternalDisp.class, ClienService.class};
		
		//Lista que se devuelve cuando el dispositivo no ha sido encontrado.
		List<T> notFoundList = new ArrayList<>();
		String deviceNotFound = "Dispositivo no encontrado";
		notFoundList.add((T) deviceNotFound);

		GenericDAO dao = new GenericDAO();
		
		//Busca en cada clase por el dispositivo segun la Mac, cuando lo encuentra para la busqueda
		//y devuelve la lista con el dispositivo.
		for (Class clazz : classes) {
			List<T> list = dao.findByProperty(clazz, "mac", mac);
			if(!list.isEmpty()) {
				return list;
			} 
		}
		//Si no encuentra devuelve una lista con la string "Dispositivo no encontrado".
		logger.info("Dispositivo no encontrado");
		return notFoundList;
		 
	}

}
