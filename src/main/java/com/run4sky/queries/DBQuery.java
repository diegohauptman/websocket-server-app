package com.run4sky.queries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.run4sky.beans.*;

/**
 * 
 * @author user
 *
 */
public class DBQuery {
	
	private static SessionFactory factory;
	
	public static List secureDispList(String sql) {
		
		Session session = factory.openSession();
	      Transaction tx = null; 
	      try {
	         tx = session.beginTransaction();
			List<SecureDisp> disps = session.createQuery(sql).list(); 
	         for (Iterator<SecureDisp> iterator = disps.iterator(); iterator.hasNext();){
	        	 SecureDisp disp = iterator.next(); 
	           System.out.println("ID: "+disp.getId());
	         }
	         tx.commit();
	         
	         return disps;
	         
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return null;
		
	}
	
	public static List internalServiceList(String sql) {
		
		Session session = factory.openSession();
	      Transaction tx = null; 
	      try {
	         tx = session.beginTransaction();
			List<InsernalService> disps = session.createQuery(sql).list(); 
	         for (Iterator<InsernalService> iterator = disps.iterator(); iterator.hasNext();){
	        	 InsernalService disp = iterator.next(); 
	           System.out.println("ID: "+disp.getId());
	         }
	         tx.commit();
	         
	         return disps;
	         
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return null;
		
	}
	
public static List externalDispList(String sql) {
		
		Session session = factory.openSession();
	      Transaction tx = null; 
	      try {
	         tx = session.beginTransaction();
			List<ExternalDisp> disps = session.createQuery(sql).list(); 
	         for (Iterator<ExternalDisp> iterator = disps.iterator(); iterator.hasNext();){
	        	 ExternalDisp disp = iterator.next(); 
	           System.out.println("ID: "+disp.getId());
	         }
	         tx.commit();
	         
	         return disps;
	         
	      } catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      } finally {
	         session.close(); 
	      }
		return null;
		
	}

public static List dispLocationList(String sql) {
	
	Session session = factory.openSession();
      Transaction tx = null; 
      try {
         tx = session.beginTransaction();
		List<DispLocation> disps = session.createQuery(sql).list(); 
         for (Iterator<DispLocation> iterator = disps.iterator(); iterator.hasNext();){
        	 DispLocation disp = iterator.next(); 
           System.out.println("ID: "+disp.getId());
         }
         tx.commit();
         
         return disps;
         
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      } finally {
         session.close(); 
      }
	return null;
	
}

public static List clientServiceList(String sql) {
	
	Session session = factory.openSession();
      Transaction tx = null; 
      try {
         tx = session.beginTransaction();
		List<ClienService> disps = session.createQuery(sql).list(); 
         for (Iterator<ClienService> iterator = disps.iterator(); iterator.hasNext();){
        	 ClienService disp = iterator.next(); 
           System.out.println("ID: "+disp.getId());
         }
         tx.commit();
         
         return disps;
         
      } catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      } finally {
         session.close(); 
      }
	return null;
	
}
	
}
