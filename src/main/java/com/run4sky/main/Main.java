package com.run4sky.main;
	import java.util.Iterator;
	import java.util.List;

	import org.hibernate.*;
	import org.hibernate.cfg.Configuration;

import com.run4sky.beans.*;

	public class Main {
		
	private static SessionFactory factory;
	
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			Main me = new Main();
			
			try {
				factory = new Configuration().configure().buildSessionFactory();
			}catch(Throwable ex) {
				System.err.println("Failed to create sessionFactory object. "+ ex);
				throw new ExceptionInInitializerError(ex);
			}
			
			System.out.println("ID del client introduit: "+me.addClient(10, "Prova","Descripcio no inscrita" ));
			me.listClientes();
			System.out.println("Borran client 10");
			me.deleteClient(10);
			me.listClientes();
			System.out.println("Update Descripcion");
			me.updateClient(0, "atope");
			me.listClientes();
			
			factory.close();
		}
		public void listClientes( ){
		      Session session = factory.openSession();
		      Transaction tx = null; 
		      try {
		         tx = session.beginTransaction();
		         String sql = "FROM Client";
				List<Client> Clients = session.createQuery(sql).list(); 
		         for (Iterator<Client> iterator = Clients.iterator(); iterator.hasNext();){
		        	 Client Client = iterator.next(); 
		           System.out.println("ID: "+Client.getId()
		        		   +"\nName: "+Client.getName()
		        		   +"\nDescripcion: "+Client.getDescription()
		        		   +"\n________________________________________");
		         }
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		   }
		/* Method to CREATE an Client in the database */
		   public Integer addClient(int id, String name, String descript){
		      Session session = factory.openSession();
		      Transaction tx = null;
		      Integer ClientID = 0;
		      	      
		      try {
		         tx = session.beginTransaction();
		         Client client = new Client(id, name, descript);
		         ClientID = (Integer) session.save(client); 
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		      return ClientID;
		   }
		 /* Method to UPDATE Description for an Client */
		   public void updateClient(Integer EmployeeID, String newDescription ){
		      Session session = factory.openSession();
		      Transaction tx = null;
		      
		      try {
		         tx = session.beginTransaction();
		         Client client = (Client)session.get(Client.class, EmployeeID); 
		         client.setDescription( newDescription );
				 session.update(client); 
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		   }
		   
		   /* Method to DELETE an Client from the Client */
		   public void deleteClient(Integer ClientID){
		      Session session = factory.openSession();
		      Transaction tx = null;
		      
		      try {
		         tx = session.beginTransaction();
		         Client employee = session.get(Client.class, ClientID); 
		         session.delete(employee); 
		         tx.commit();
		      } catch (HibernateException e) {
		         if (tx!=null) tx.rollback();
		         e.printStackTrace(); 
		      } finally {
		         session.close(); 
		      }
		   }
	}


