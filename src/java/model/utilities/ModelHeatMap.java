package model.utilities;

import db.controller.DAO;
import db.pojos.Captacion;
import db.pojos.Catalogocuenta;
import db.pojos.Catalogominimo;
import db.pojos.Cuenta;
import db.pojos.Disponibilidad;
import db.pojos.Ingresosnetos;
import db.pojos.Prestamo;
import db.pojos.Regcuenta;
import db.pojos.Reservas;
import db.pojos.Tarjetacredito;
import db.pojos.Valores;
import interpreter.MathInterpreterException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.executor.ModelExecutor;
import util.Util;

public class ModelHeatMap {

    //el ejercicio sobre el cual se va a hacer el mapa de calor
    private Regcuenta regCuentaOriginal;
    //el objeto al cual se le movera el valor en el ejeX
    private Object targetX;
    //el objeto al cual se le movera el valor en el ejeY
    private Object targetY;
    //valores que va a adquirir el objetoX
    private Double[] valuesXaxis;
    //valores que va a adquirir el objetoY
    private Double[] valuesYaxis;
    //resultado que se quiere
    private Catalogocuenta cuentaResult;

    //almacenaran al objetoX y objetoY de los inputs par ya solo actualizar estos valores en cada ejecucion del modelo
    private Object referenciaObjetoX;
    private Object referenciaObjetoY;

    public ModelHeatMap(Regcuenta regCuentaOriginal, Object targetX, Object targetY, Double[] valuesXaxis, Double[] valuesYaxis, Catalogocuenta cuentaResult) {
        List<Regcuenta> ejerciciosCalculados = DAO.getEjerciciosCalculados();
        for (Regcuenta r : ejerciciosCalculados) {
            if (r.getIdRegCuenta() == regCuentaOriginal.getIdRegCuenta()) {
                this.regCuentaOriginal = r;
            }
        }
        this.targetX = targetX;
        this.targetY = targetY;
        this.valuesXaxis = valuesXaxis;
        this.valuesYaxis = valuesYaxis;
        this.cuentaResult = cuentaResult;
    }

    /**
     * proceso para copiar un ejercicio, solo copia valores iniciales, no copia
     * nada de las cuentas calculadas
     */
    private Regcuenta copiarProyecto() {
        Regcuenta regCuenta = null;
        try {
            regCuenta = new Regcuenta();
            regCuenta.setDesRegCuenta("tmpHeatMap");
            regCuenta.setFecha(regCuentaOriginal.getFecha());
            DAO.save(regCuenta);
            copiarCuentas(regCuenta);
            return regCuenta;
        } catch (CloneNotSupportedException ex) {
            DAO.delete(regCuenta);
            Logger.getLogger(ModelHeatMap.class.getName()).log(Level.INFO, null, ex);
            return null;
        }

    }

    /**
     * copia cada una de las cuentas iniciales del proyecto
     *
     * @param regNuevo
     * @throws CloneNotSupportedException
     */
    private void copiarCuentas(Regcuenta regNuevo) throws CloneNotSupportedException {
        DAO.refresh(regCuentaOriginal);
        //prestamos
        Set<Prestamo> prestamos = regCuentaOriginal.getPrestamos();
        //captacion
        Set<Captacion> captacions = regCuentaOriginal.getCaptacions();
        //catalogo
        Set<Catalogominimo> catMin = regCuentaOriginal.getCatalogominimos();
        //disponibilidades
        Set<Disponibilidad> disponibilidades = regCuentaOriginal.getDisponibilidads();
        //ingresos
        Set<Ingresosnetos> ingresos = regCuentaOriginal.getIngresosnetoses();
        //reservas
        Set<Reservas> reservas = regCuentaOriginal.getReservases();
        //tarjeta
        Set<Tarjetacredito> tarjeta = regCuentaOriginal.getTarjetacreditos();
        //tenencia
        Set<Valores> valores = regCuentaOriginal.getValoreses();
        List<Object> items = new LinkedList<Object>();
        Integer idTargetX = invokeIdHeatMap(targetX);
        Integer idTargetY = targetY == null ? null : invokeIdHeatMap(targetY);
        Class claseX = targetX.getClass();
        Class claseY = targetY == null ? null : targetY.getClass();
        System.out.println("el modificado "+idTargetX);
        for (Captacion c : captacions) {
            Captacion nueva = new Captacion(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCuentaCaptacion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
            if (idTargetX - invokeIdHeatMap(c) == 0 && c.getClass() == claseX) {
                referenciaObjetoX = nueva;
            }
            if (invokeIdHeatMap(c) -idTargetY==0 && c.getClass() == claseY) {
                referenciaObjetoY = nueva;
            }
        }
        for (Catalogominimo c : catMin) {
            Catalogominimo nueva = new Catalogominimo(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getValor(), c.getMoneda());
            items.add(nueva);
        }
        for (Disponibilidad c : disponibilidades) {
            Disponibilidad nueva = new Disponibilidad(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getMonto(), c.getFechaVencimiento());
            items.add(nueva);
            if (idTargetX - invokeIdHeatMap(c) == 0 && c.getClass() == claseX) {
                referenciaObjetoX = nueva;
            }
            if (invokeIdHeatMap(c) -idTargetY==0 && c.getClass() == claseY) {
                referenciaObjetoY = nueva;
            }
        }
        for (Ingresosnetos c : ingresos) {
            Ingresosnetos nueva = new Ingresosnetos(regNuevo, c.getFecha(), c.getNumeroMes(), c.getIngresoNeto());
            items.add(nueva);
        }
        for (Reservas c : reservas) {
            Reservas nueva = new Reservas(regNuevo, c.getFecha(), c.getEstatusCrediticio(), c.getMonto());
            items.add(nueva);
        }
        for (Tarjetacredito c : tarjeta) {
            Tarjetacredito nueva = new Tarjetacredito(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCredito(), c.getSaldoInsoluto(), c.getFechaCorte(), c.getTipoTarjeta(), c.getRelevante());
            items.add(nueva);
            if (idTargetX - invokeIdHeatMap(c) == 0 && c.getClass() == claseX) {
                referenciaObjetoX = nueva;
            }
            if (invokeIdHeatMap(c) -idTargetY==0 && c.getClass() == claseY) {
                referenciaObjetoY = nueva;
            }
        }
        for (Valores c : valores) {
            Valores nueva = new Valores(regNuevo, c.getFecha(), c.getIdCuentaContable(), c.getDescripcion(), c.getNumeroTitulos(), c.getTipoValor(), c.getEmision(), c.getSerie(), c.getFechaProximoCupon(), c.getGrupoRc10(), c.getPrecio(), c.getSobretasa(), c.getCalificacion(), c.getGrupoRc07(), c.getPonderador(), c.getPlazo(), c.getFechaVencimiento(), c.getMoneda(), c.getGradoRiesgo());
            items.add(nueva);
            if (idTargetX - invokeIdHeatMap(c) == 0 && c.getClass() == claseX) {
                referenciaObjetoX = nueva;
            }
            if (invokeIdHeatMap(c) -idTargetY==0 && c.getClass() == claseY) {
                referenciaObjetoY = nueva;
            }
        }
        for (Prestamo c : prestamos) {
            Prestamo nueva = new Prestamo(regNuevo, c.getCatalogocuenta(), c.getFecha(), c.getDescripcion(), c.getIdCuentaPrestamo(), c.getSaldo(), c.getFechaDeCorte(), c.getTipoPrestamo(), c.getRelevante());
            items.add(nueva);
            if (idTargetX - invokeIdHeatMap(c) == 0 && c.getClass() == claseX) {
                referenciaObjetoX = nueva;
            }
            if (invokeIdHeatMap(c) -idTargetY==0 && c.getClass() == claseY) {
                referenciaObjetoY = nueva;
            }
        }
        DAO.saveMultiple(items);
    }

    public Integer invokeIdHeatMap(Object ob) {
        return (Integer) Util.reflectionInvoke(ob, "getIdHeatMap");
    }

    public Map<String, Double> calcHeatMap() {
        ModelExecutor executor = null;
        Map<String, Double> mapResult = new HashMap<String, Double>();
        Regcuenta regCuentaCopia = copiarProyecto();
        for (int x = 0; x < valuesXaxis.length; x++) {
            updateValues(referenciaObjetoX, valuesXaxis[x]);
            if (targetY == null || valuesYaxis == null || valuesYaxis.length == 0) {
                Map<String, Cuenta> resultadoMap = calcularModelo(executor, regCuentaCopia);
                Double resultado = sacarResultado(resultadoMap);
                mapResult.put(x + ",0", resultado);
            } else {
                for (int y = 0; y < valuesYaxis.length; y++) {
                    updateValues(referenciaObjetoY, valuesXaxis[y]);
                    Map<String, Cuenta> resultadoMap = calcularModelo(executor, regCuentaCopia);
                    Double resultado = sacarResultado(resultadoMap);
                    mapResult.put(x + "," + y, resultado);
                }
            }

        }
        DAO.delete(regCuentaCopia);
        return mapResult;
    }

    private void updateValues(Object targetUpdate, Double newValue) {
        Util.reflectionInvokeSet(targetUpdate, Util.getMethod(targetUpdate.getClass(), "setValorHeatMap", Double.class), newValue);
        DAO.update(targetUpdate);
    }

    private Map<String, Cuenta> calcularModelo(ModelExecutor executor, Regcuenta regCuenta) {
        DAO.refresh(regCuenta);
        try {
            executor = new ModelExecutor(regCuenta, true);
            return executor.start();
        } catch (Exception ex) {

            Logger.getLogger(ModelHeatMap.class.getName()).log(Level.INFO, null, ex);
            return null;
        }
    }

    private Double sacarResultado(Map<String, Cuenta> resultadoMap) {
        return resultadoMap.get(cuentaResult.getIdCatalogoCuenta().toString()).getValor();
    }

    public static void main(String[] args) {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        Regcuenta get = createQuery.get(1);
        System.out.println("el regcta" + get.getDesRegCuenta());
        
        //primera variable
        Set valoreses = get.getTarjetacreditos();
        Tarjetacredito next = (Tarjetacredito) valoreses.iterator().next();
        Double monto = next.getSaldoInsoluto();
        
        
        DecimalFormat format=new DecimalFormat("###,###,###.##");
        
        Double[] array=new Double[]{monto * .02  , monto * .04, monto * .06, monto * .08, monto * 1,monto * 1.02  , monto * 1.04, monto * 1.06, monto * 1.08, monto * 2};
        for(Double d:array){
            System.out.print(format.format(d)+"\t\t");
        }
        System.out.println("");
        //segunda variable
        Set<Valores> valoreses1 = get.getValoreses();
        Valores next1 = valoreses1.iterator().next();
        Double monto1 = next1.getMonto();
        Double[] array2=new Double[]{monto1 * .02  , monto1 * .04, monto1 * .06, monto1 * .08, monto1 * 1,monto1 * 1.02  , monto1 * 1.04, monto1 * 1.06, monto1 * 1.08, monto1 * 2};
        
        for(Double d:array2){
            System.out.print(format.format(d)+"\t\t");
        }
        
        Catalogocuenta deseado = null;
        List<Catalogocuenta> createQuery1 = DAO.createQuery(Catalogocuenta.class, null);
        for (Catalogocuenta c : createQuery1) {
            if (c.getIdCatalogoCuenta().toString().equals("1")) {
                deseado = c;
            }
        }
        
        
        
        ModelHeatMap heatm = new ModelHeatMap(get, next, next1, array, array2, deseado);
        Map<String, Double> calcHeatMap = heatm.calcHeatMap();
            
        System.out.println("x tarjeta"+next.getIdCredito()+" original "+next.getSaldoInsoluto());
        System.out.println("y valores"+next1.getTipoValorEmisionSerie()+" original "+next1.getMonto());
        for (int y = array2.length-1;y>=0; y--) {
            System.out.print(format.format(array2[y])+"\t\t");
            for(int x=0;x<array.length;x++){
                System.out.print(x+","+y);
            }
            System.out.println("");
        }
        System.out.print("ICAP \t\t");
        for(int x=0;x<array.length;x++){
            System.out.print(format.format(array[x])+"\t\t");
        }
        
        System.out.println("");
        System.out.println("\n\n\n");
        
        for (int y = array2.length-1;y>=0; y--) {
            System.out.print(format.format(array2[y])+"\t\t");
            for(int x=0;x<array.length;x++){
                System.out.print(format.format(calcHeatMap.get(x+","+y)));
            }
            System.out.println("");
        }
        System.out.print("ICAP \t\t");
        for(int x=0;x<array.length;x++){
            System.out.print(format.format(array[x])+"\t\t");
        }
    }

}
