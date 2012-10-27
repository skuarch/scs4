package dao;

import java.util.List;
import net.ConnectAction;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author skuarch
 */
public class DAO {

    private Session session = null;

    //==========================================================================
    /**
     * create a instance.for use this class please don't create a object,
     * <br /> only used new DAO().method();
     * @throws Exception 
     */
    public DAO() throws Exception {
        startSession();
    } // end DAO

    //==========================================================================
    /**
     * start session with hibernate.
     * @throws Exception 
     */
    private void startSession() throws Exception {

        try {

            session = HibernateUtil.getSessionFactory().openSession();        
            
        } catch (Exception e) {
            HibernateUtil.closeSession(session);            
            throw e;
        }
    } // end startSession

    //==========================================================================
    private void handlerError(Exception e) throws Exception {
        try {

            throw new Exception(e);

        } catch (Exception ex) {
            throw ex;
        } finally {
            HibernateUtil.closeSession(session);
        }
    } // end handlerError

    //==========================================================================
    /**
     * save object in database.
     * @param object Object
     * @return long (id)
     * @throws Exception 
     */
    public long create(Object object) throws Exception {

        if (object == null) {
            throw new NullPointerException("object is null");
        }

        long id = 0;

        try {

            id = (Long) session.save(object);
            session.beginTransaction().commit();

        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return id;

    } // end create

    //==========================================================================
    /**
     * update a object in database.
     * @param object Object
     * @throws Exception 
     */
    public void update(Object object) throws Exception {

        try {
            session.update(object);
            session.beginTransaction().commit();
        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

    } // end update

    //==========================================================================
    /**
     * 
     * @param id long
     * @param clazz Class
     * @return Object
     * @throws Exception 
     */
    public Object get(long id, Class clazz) throws Exception {

        Object object = null;

        try {

            object = session.get(clazz, id);
            session.beginTransaction().commit();

        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return object;
    } // end get

    //==========================================================================
    /**
     * delete object in database.
     * @param object Object
     * @throws Exception 
     */
    public void delete(Object object) throws Exception {

        try {
            session.delete(object);
        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }
    } // end delete

    //==========================================================================
    /**
     * return in a list all objects
     * @param stringClass String
     * @return List
     * @throws Exception 
     */
    public List getAll(String stringClass) throws Exception {

        List list = null;

        try {

            list = session.createQuery("from " + stringClass).list();

        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return list;

    } // end getAll

    //==========================================================================
    /**
     * search in database using a like, example:
     * select * from table where filed like "stringToFind%";
     * @param clazz Class
     * @param stringToFind String
     * @param field String
     * @return List
     * @throws Exception 
     */
    public List find(Class clazz, String stringToFind, String field) throws Exception {

        List objects = null;
        Criteria criteria = null;

        try {

            criteria = session.createCriteria(clazz);
            criteria.add(Restrictions.like(field, stringToFind + "%"));
            objects = criteria.list();

        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return objects;

    } // end find

    //==========================================================================
    /**
     * truncate database.
     * @param table String
     * @throws Exception 
     */
    public void deleteAll(String table) throws Exception {

        try {

            new ConnectAction().update("truncate table " + table);

        } catch (Exception e) {
            throw e;
        }

    } // end deleteAll

    //==========================================================================
    /**
     * execute a hsql sentence.
     * @param hsql String
     * @return List
     * @throws Exception 
     */
    public List hsql(String hsql) throws Exception {

        List list = null;

        try {
            
            list = session.createQuery(hsql).list();

        } catch (Exception e) {
            handlerError(e);
        } finally {
            HibernateUtil.closeSession(session);
        }

        return list;
    } // end hsql
    
} // end class