package db.controller;

import db.pojos.Cuenta;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Tipousuario;
import db.pojos.Tracking;
import db.pojos.User;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;

/**
 * Se encarga de entablar la comunicacion con la base de datos
 *
 * @author WWN
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
        try {

            List list = createCriteria.list();

            return list;
        } catch (Exception e) {
            return new LinkedList();
        }
    }

    /**
     * guarda un registro dentro del tracking log
     *
     * @param user
     * @param text
     */
    public static void saveRecordt(User user, String text) {
        Tracking tracking = new Tracking(user, text, Calendar.getInstance().getTime());
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

    /**
     * guarda un solo elemento
     *
     * @param object
     */
    public static void save(Object object) {
        session.save(object);
        session.flush();
        session.clear();
    }

    /**
     * guarda o actualiza el elemento del parametro
     *
     * @param obj
     */
    public static void saveOrUpdate(Object obj) {
        session.saveOrUpdate(obj);
        session.flush();
        session.clear();
    }

    /**
     * actualiza el elemento del parametro, debe existir previamente en la base
     * de datos
     *
     * @param obj
     */
    public static void update(Object obj) {
        session.update(obj);
        session.flush();
        session.clear();
    }

    /**
     * borra un elemento de la base de datos
     *
     * @param object
     */
    public static void delete(Object object) {
        session.delete(object);
        session.flush();
        session.clear();
    }

    /**
     * actualiza un registro para obtener sus nuevos valores
     *
     * @param obj
     */
    public static void refresh(Object obj) {
        session.refresh(obj);
    }

    /**
     * guarda multiples objetos dentro de la base, todos deben ser instancias de
     * objetos mapeados
     *
     * @param objects
     */
    public static void saveMultiple(Collection objects) {
        Transaction tr = session.beginTransaction();
        tr.begin();
        for (Object o : objects) {
            session.save(o);
        }
        tr.commit();
        session.flush();
        session.clear();
    }

    /**
     * borra multiples objetos dentro de la base, todos deben estar almacenados
     * en base previamente
     *
     * @param list
     */
    public static void deletemultiple(Collection list) {
        for (Object o : list) {
            session.delete(o);
        }

        session.flush();
        session.clear();

    }

    /**
     * ejecuta codigo sql dado por el usuario
     *
     * @param sql
     */
    public static void executeSQL(String sql) {
        checkSession();

        SQLQuery createSQLQuery = session.createSQLQuery(sql);
        createSQLQuery.executeUpdate();
    }

    public static void main(String[] args) {
        //List<Tipousuario> createQuery = DAO.createQuery(Tipousuario.class,null);
        //System.out.println(createQuery.get(0).getUsuarios().iterator().next());
     
    }

    public static List<Regcuenta> getEjerciciosCalculados(){
        List<Cuenta> createQuery = DAO.createQuery(Cuenta.class, null);
        Set<Integer> ejerciciosIds=new HashSet<Integer>();
        for(Cuenta r:createQuery){
            if(r.getCatalogocuenta().getIdCatalogoCuenta()==1){
                ejerciciosIds.add(r.getRegcuenta().getIdRegCuenta());
            }
        }
        List<Regcuenta> resp=new LinkedList<Regcuenta>();
        List<Regcuenta> createQuery1 = DAO.createQuery(Regcuenta.class, null);
        for(Regcuenta r:createQuery1){
            if(ejerciciosIds.contains(r.getIdRegCuenta())){
                resp.add(r);
            }
        }
        return resp;
    }
    
}
