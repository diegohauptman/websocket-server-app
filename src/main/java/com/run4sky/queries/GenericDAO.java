package com.run4sky.queries;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.run4sky.beans.SecureDisp;

public class GenericDAO<T> {

	private static SessionFactory factory;

	public List list(String sql, T tabla) {

		Session session = factory.openSession();
		Transaction tx = null;
		
		if (tabla instanceof Class<?>) {
			
		}
		
		try {
			tx = session.beginTransaction();
			List<Class<?>> list = session.createQuery(sql).list();
//			for (Iterator<SecureDisp> iterator = disps.iterator(); iterator.hasNext();) {
//				SecureDisp disp = iterator.next();
//				System.out.println("ID: " + disp.getId());
//			}
			tx.commit();
			return list;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}
	
	public Class<? extends Object> getType(T item) {
		return item.getClass();
	}

}
