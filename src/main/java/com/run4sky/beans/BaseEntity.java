/**
 * 
 */
package com.run4sky.beans;

import java.io.Serializable;

import org.hibernate.proxy.HibernateProxyHelper;

/**
 * Clase base para todas la entities.
 * 
 * @author Diego
 *
 */
public abstract class BaseEntity <T extends Serializable>{
	
	/**
	 * Este metodo deve retornar la clave primaria.
	 * @return
	 */
	public abstract T getId();
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BaseEntity)) {
			return false;
		}
		if (getId() == null || ((BaseEntity<?>) obj).getId() == null) {
			return false;
		}
		if (!getId().equals(((BaseEntity<?>) obj).getId())) {
			return false;
		}
		if (!HibernateProxyHelper.getClassWithoutInitializingProxy(obj)
				.isAssignableFrom(this.getClass())) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		return getId() == null ? super.hashCode() : getId().hashCode();
	}

}
