package com.run4sky.queries;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javassist.NotFoundException;

public class GenericDAO {
	
	
	private static SessionFactory factory;
	
	public GenericDAO(){
		
		try {
			factory = new Configuration().configure().buildSessionFactory();
		}catch(Throwable ex) {
			System.err.println("Failed to create sessionFactory object. "+ ex);
			throw new ExceptionInInitializerError(ex);
		}
		
	}
	
	/**
	 * Metodo generico para buscar datos en cualquier tabla/classe (objeto java)
	 * utilizando com criterio una de sus propriedades (por ejemplo, la mac del dispositivo)
	 * 
	 * Ejemplo de uso:
	 * 
	 * List<SecureDisp> dispositivosDeSeguridad = dao.findByProperty(SecureDisp.class, "mac", "A1:B2:C3:D4:E5:F6");
	 * 
	 * @param clazz
	 * @param propertyName
	 * @param value
	 * @return List 
	 */
	public <T> List<T> findByProperty(Class<T> clazz, String propertyName, Object value) {
		//crea la sesion de hibernate
		Session session = factory.openSession();
		//construye la criteria de JPA para una entidad de persistencia (classe)
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		//establece el criterio de busqueda
		criteria.where(builder.equal(root.get(propertyName), value));
		List<T> list = session.createQuery(criteria).getResultList();
		return list;
	}
	
	/**
	 * Metodo generico para guardar
	 * @param entity
	 */
	public <T> void save(T entity) {
		Session session = factory.openSession();
		try {
			session.getTransaction().begin();
			session.save(entity);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if (session.getTransaction() != null) 
				session.getTransaction().rollback();
			
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Metodo generico para duscar por id
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T findById(Class<T> clazz, Serializable id) {
		Session session = factory.openSession();
		T t = session.find(clazz, id);
		session.close();
		return t;
	}
	
	/**
	 * Metodo generico que elimina in objeto de la tabla por el id.
	 * @param clazz
	 * @param id
	 * @throws NotFoundException
	 */
	public <T> void delete(Class<T> clazz, Serializable id) throws NotFoundException {
		Session session = factory.openSession();
		
		T entity = session.get(clazz, id);
		if(entity != null) {
			session.getTransaction().begin();
			session.delete(entity);
			session.getTransaction().commit();
			session.close();
		} else {
			throw new NotFoundException("No encontrado");
		}
		
	}
	
	/**
	 * Metodo para imprimir una lista en consola.
	 * @param list
	 */
	public <T> void printList(List<T> list) {
		for (Iterator<T> iterator = list.iterator(); iterator.hasNext();){
        	 T t = iterator.next(); 
           System.out.println(t.toString()
        		   +"\n________________________________________");
         }
	}
	
	
}
