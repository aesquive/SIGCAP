package db.controller;

import db.pojos.Permisos;
import db.pojos.Tracking;
import db.pojos.User;
import java.util.Calendar;
import java.util.List;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

/**
 * Se encarga de entablar la comunicacion con la base de datos
 *
 * @author zorin
 */
public class DAO {

    /**
     * conexion con la base de datos
     */
    private static Session session;

    /**
     * ejecuta un query pasandole la clase sobre la cual se hara el query con
     * los criterios mencionados
     *
     * @param clase
     * @param criterios
     * @return
     */
    public static List createQuery(Class clase, Criterion[] criterios) {
        checkSession();
        Criteria createCriteria = session.createCriteria(clase);
        if (criterios != null) {
            for (Criterion cr : criterios) {
                createCriteria.add(cr);
            }
        }
        List list = createCriteria.list();

        return list;
    }
    
    public static void saveRecordt(User user , String text){
        Tracking tracking= new Tracking(user, text, Calendar.getInstance().getTime());
        save(tracking);
    }

    /**
     * verifica la sesion y la abre si es necesario
     */
    private static void checkSession() {
        if (session == null || !session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }
        session.reconnect();
        session.setCacheMode(CacheMode.IGNORE);
    }

    public static void save(Object object) {
        session.save(object);
        session.flush();
        session.clear();
    }

    public static void saveOrUpdate(Object obj) {
        session.saveOrUpdate(obj);
        session.flush();
        session.clear();
    }

    public static void update(Object obj) {
        session.update(obj);
        session.flush();
        session.clear();
    }

    public static void main(String[] args) {
        
    }

    public static void delete(Object object) {
        session.delete(object);
        session.flush();
        session.clear();
    
    }

    public static void executeSQL(String string) {
        System.out.println("el sql que correra el dao "+string);
        Transaction tr=session.beginTransaction();
        SQLQuery createSQLQuery = session.createSQLQuery(string);
        int executeUpdate = createSQLQuery.executeUpdate();
        tr.commit();
        session.flush();
        session.clear();
        session.close();
        session=null;
        checkSession();
    }

    public static void refresh(Object obj){
        session.refresh(obj);
    }
    
    public static void saveUpdateMultiple(List<Object> obj){
        for(Object o:obj){
            session.saveOrUpdate(o);
        }
        session.flush();
        session.clear();
    }
    
    public static void saveCargaDatos(List<Object>... objects){
        for(List<Object> list:objects){
            for(Object o:list){
                session.saveOrUpdate(o);
            }
        }
        session.flush();
        session.clear();
    }
}
