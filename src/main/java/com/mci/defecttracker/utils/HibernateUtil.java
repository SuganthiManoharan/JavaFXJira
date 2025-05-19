package com.mci.defecttracker.utils;

import org.hibernate.SessionFactory;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/** 
 * Helper class that handles startup and that accesses Hibernate's SessionFactory
 * to obtain a Session object.
 * @author Suganthi Manoharan
 * @author MCI DIBSE
 * @author Software Engineering II 
*/
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

	/**
	* Utility method to create hibernate session factory object
	*  
	* @return  the session factory object
	* @see <a href="https://docs.jboss.org/hibernate/orm/5.1/javadocs/org/hibernate/SessionFactory.html">SessionFactory</a>
	*/
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
            	
                registry = new StandardServiceRegistryBuilder().configure().build();

                MetadataSources sources = new MetadataSources(registry);

                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}