package com.run4sky.network;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;

/**
 * Clase que recupera la IP publica/externa de la sesion del remote endpoint.
 * @author D. Hauptman
 *
 */
public class GetPublicIP {
	
	public static InetSocketAddress getPublicIP(Session session) {
	    if(session == null){
	        return null;
	    }

	    Async async = session.getAsyncRemote();
	    InetSocketAddress addr = (InetSocketAddress) getFieldInstance(async, 
	    		"base#socketWrapper#socket#sc#remoteAddress");

	    return addr;
	}

	private static Object getFieldInstance(Object obj, String fieldPath) {
	    String fields[] = fieldPath.split("#");
	    for(String field : fields) {
	        obj = getField(obj, obj.getClass(), field);
	        if(obj == null) {
	            return null;
	        }
	    }

	    return obj;
	}

	private static Object getField(Object obj, Class<?> clazz, String fieldName) {
	    for(;clazz != Object.class; clazz = clazz.getSuperclass()) {
	        try {
	            Field field;
	            field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            return field.get(obj);
	        } catch (Exception e) {
	        }            
	    }

	    return null;
	}

}
