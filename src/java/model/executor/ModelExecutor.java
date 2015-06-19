package model.executor;

import db.controller.DAO;
import db.pojos.Catalogocuenta;
import db.pojos.Cuenta;
import db.pojos.Moneda;
import db.pojos.Operacion;
import db.pojos.Regcuenta;
import interpreter.MathInterpreter;
import interpreter.MathInterpreterException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.wrappers.BaseModeloMathInterpreter;
import org.hibernate.criterion.Criterion;

/**
 * Ejecuta todas las operaciones contenidas dentro de la base de datos de la
 * tabla operacion Es un sistema inteligente que si utiliza un valor que no ha
 * sido calculado aun va a calcularlo , si no hay forma de calcular el valor
 * entonces lanzara una excepcion.
 *
 * @author WWN
 */
public class ModelExecutor {

    /**
     * son las cuentas con las cuales se va a correr el modelo
     */
    public Map<String, Cuenta> cuentas;
    public Map<String, Double> valores;
    public Map<String, Operacion> operaciones;
    public Regcuenta regCuenta;
    private boolean updateValues;
    private Criterion[] criterios;
   
    public ModelExecutor(Regcuenta regCuenta, boolean updateDataBase) throws IOException {
        this.updateValues = updateDataBase;
        this.valores = new HashMap<String, Double>();
        this.cuentas = new HashMap<String, Cuenta>();
        this.operaciones = mapOperaciones(DAO.createQuery(Operacion.class, null));
        this.regCuenta = regCuenta;
        //sacamos las cuentas base del calculo
        BaseModeloMathInterpreter base = new BaseModeloMathInterpreter(regCuenta);
        base.calculate();
        //este map trae el resultado de todo el modelo, no la parte de los rcs, si no la contable
        Map<String, Double> map = base.getMap();
        
        //en caso de que se tenga updateValues=true sera necesario eliminar las cuentas antes calculadas
        if(updateValues){DAO.executeSQL("delete from cuenta where idRegCuenta="+regCuenta.getIdRegCuenta());}
        
        //sacamos los datos del mapeo de catalogos de cuentas
        List<Catalogocuenta> queryCatalogos = DAO.createQuery(Catalogocuenta.class, criterios);
        Map<String, Catalogocuenta> mapCatalogosCuentas = new HashMap<String, Catalogocuenta>();
        for (Catalogocuenta c : queryCatalogos) {
            mapCatalogosCuentas.put(c.getIdCatalogoCuenta().toString(), c);
        }
        List<Moneda> createQuery = DAO.createQuery(Moneda.class, null);
        Moneda peso = null;
        for (Moneda m : createQuery) {
            if (m.getIdMoneda() == 14) {
                peso = m;
            }
        }
        
        //generamos las nuevas cuentas de valores iniciales del calculo    ESTO NO SE DEBE HACER SI SOLO SE QUIERE CORRER UN ESCENARIO
        //SOLO SE DEBE HACER SI SE QUIERE MODIFICAR LAS CUENTAS DEL PROYECTO
        
            for (String s : map.keySet()) {
                Cuenta c = new Cuenta();
                c.setCatalogocuenta(mapCatalogosCuentas.get(s));
                c.setValor(map.get(s));
                c.setRegcuenta(regCuenta);
                c.setMoneda(peso);
                c.setRef("");
                cuentas.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(),c);
                valores.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(),c.getValor());
            }
            
        
            
    }

    public Map<String, Cuenta> start() throws MathInterpreterException {
        System.out.println("-----CALCULO INICIADO-----");
        startOperations();
        if(updateValues){
            List<Cuenta> tmps=new LinkedList<Cuenta>();
            for(String k:cuentas.keySet()){
                tmps.add(cuentas.get(k));
            }
            DAO.saveMultiple(tmps);
        }
        System.out.println("-----CALCULO TERMINADO-----");
        return cuentas;
    }

    /**
     * mapea las operaciones con el numero de identificacion de la cuenta
     *
     * @param listaOperaciones
     * @return
     */
    private Map<String, Operacion> mapOperaciones(List<Operacion> listaOperaciones) {
        Map<String, Operacion> ops = new HashMap<String, Operacion>();
        for (Operacion op : listaOperaciones) {
            ops.put(op.getCatalogocuenta().getIdCatalogoCuenta().toString(), op);
        }
        return ops;
    }

    /**
     * empieza a realizar las operaciones
     */
    private void startOperations() throws MathInterpreterException {
        
        for (String s : operaciones.keySet()) {
            makeOperacion(operaciones.get(s));
        }
        
    }


    /**
     * realiza todas las operaciones del modelo
     *
     * @param operacion
     * @throws MathInterpreterException
     */
    private void makeOperacion(Operacion operacion) throws MathInterpreterException {
        if(cuentas.get(operacion.getCatalogocuenta().getIdCatalogoCuenta().toString())!=null){
            return;
        }
        //System.out.println("haciendo la operacion de " + operacion.getCatalogocuenta().getIdCatalogoCuenta());     
        String valOperacion = operacion.getValOperacion();
        String[] split = valOperacion.split("=");
        String ctasRef = "";
        //checamos que todas las cuentas a las que hace referencia una operacion ya esten hechas
        for (int t = 1; t < split.length; t++) {
            String idRef = "";
            for (int i = 0; i < split[t].length(); i++) {
                if (split[t].charAt(i) == ')') {
                    i = split[t].length();
                } else {
                    idRef = idRef + split[t].charAt(i);
                }
            }
            //checamos si necesitamos calcular alguna otra cosa ante
            if (cuentas.get(idRef) == null && operaciones.get(idRef) != null) {
                makeOperacion(operaciones.get(idRef));

            }
            //agregamos 
            Cuenta ctaRef = cuentas.get(idRef);
            if (ctaRef != null) {
                ctasRef = ctasRef.equals("") ? ctaRef.getCatalogocuenta().getIdCatalogoCuenta()+ "," : ctasRef + ctaRef.getCatalogocuenta().getIdCatalogoCuenta()+ ",";
            }
        }
        //a este momento ya todas las cuentas de referencia deben estar hechas , entonces ahora si realizamos la operacion
        String interp = MathInterpreter.interp(operacion.getValOperacion(), valores);
        interp = interp == null ? "0.0" : interp;
        Cuenta nueva = cuentas.get(operacion.getCatalogocuenta().getIdCatalogoCuenta().toString());
        if (nueva != null) {
            nueva.setRegcuenta(regCuenta);
            nueva.setCatalogocuenta(operacion.getCatalogocuenta());
            nueva.setValor(Double.valueOf(interp));
            nueva.setRef(ctasRef);
            nueva.setStatus(0);
        } else {
            nueva = new Cuenta(null, regCuenta, operacion.getCatalogocuenta(), Double.valueOf(interp), ctasRef, 0);
        }
        cuentas.put(operacion.getCatalogocuenta().getIdCatalogoCuenta().toString(), nueva);
        valores.put(operacion.getCatalogocuenta().getIdCatalogoCuenta().toString(), Double.valueOf(interp));
    }

    /**
     * prueba
     *
     * @param args
     * @throws MathInterpreterException
     */
    public static void main(String[] args) throws MathInterpreterException, IOException {
        int regCuenta = 23;
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        Regcuenta c = new Regcuenta();
        for (Regcuenta r : createQuery) {
            if (r.getIdRegCuenta() == regCuenta) {
                c = r;
            }
        }
        ModelExecutor m = new ModelExecutor(c,true);
        Map<String, Cuenta> start = m.start();
        System.out.println("acabo y el valor de 1 es :");
        System.out.println(start.get("1").getValor());
        System.out.println(start.get("93916").getValor());
    }

}
