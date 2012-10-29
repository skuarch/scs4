package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory = null;
    private static ServiceRegistry serviceRegistry;

    //==========================================================================
    static {
        try {
            
            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        } catch (Exception e) {
            try {
                throw e;                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //==========================================================================
    public static void closeSession(Session session) {

        if (session != null) {

            if (session.isOpen()) {
                session.close();
            }
        }

    }

    //==========================================================================
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
} // end class