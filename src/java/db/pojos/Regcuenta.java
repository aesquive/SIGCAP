package db.pojos;
// Generated 26/06/2014 12:43:36 AM by Hibernate Tools 3.6.0

import db.controller.DAO;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.NDimensionVector;
import util.Util;


/**
 * Regcuenta generated by hbm2java
 */
public class Regcuenta  implements java.io.Serializable,Cloneable {


     private Integer idRegCuenta;
     private String desRegCuenta;
     private Date fecha;
     private Set catalogominimos = new HashSet(0);
     private Set disponibilidads = new HashSet(0);
     private Set ingresosnetoses = new HashSet(0);
     private Set valoreses = new HashSet(0);
     private Set tarjetacreditos = new HashSet(0);
     private Set reservases = new HashSet(0);
     private Set prestamos = new HashSet(0);
     private Set regvectors = new HashSet(0);
     private Set regcuentausers = new HashSet(0);
     private Set cuentas = new HashSet(0);
     private Set captacions = new HashSet(0);

    public Regcuenta() {
    }

	
    public Regcuenta(String desRegCuenta) {
        this.desRegCuenta = desRegCuenta;
    }
    public Regcuenta(String desRegCuenta, Date fecha, Set catalogominimos, Set disponibilidads, Set ingresosnetoses, Set valoreses, Set tarjetacreditos, Set reservases, Set prestamos, Set regvectors, Set regcuentausers, Set cuentas, Set captacions) {
       this.desRegCuenta = desRegCuenta;
       this.fecha = fecha;
       this.catalogominimos = catalogominimos;
       this.disponibilidads = disponibilidads;
       this.ingresosnetoses = ingresosnetoses;
       this.valoreses = valoreses;
       this.tarjetacreditos = tarjetacreditos;
       this.reservases = reservases;
       this.prestamos = prestamos;
       this.regvectors = regvectors;
       this.regcuentausers = regcuentausers;
       this.cuentas = cuentas;
       this.captacions = captacions;
    }
   
    public Integer getIdRegCuenta() {
        return this.idRegCuenta;
    }
    
    public void setIdRegCuenta(Integer idRegCuenta) {
        this.idRegCuenta = idRegCuenta;
    }
    public String getDesRegCuenta() {
        return this.desRegCuenta;
    }
    
    public void setDesRegCuenta(String desRegCuenta) {
        this.desRegCuenta = desRegCuenta;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public Set getCatalogominimos() {
        return this.catalogominimos;
    }
    
    public void setCatalogominimos(Set catalogominimos) {
        this.catalogominimos = catalogominimos;
    }
    public Set getDisponibilidads() {
        return this.disponibilidads;
    }
    
    public void setDisponibilidads(Set disponibilidads) {
        this.disponibilidads = disponibilidads;
    }
    public Set getIngresosnetoses() {
        return this.ingresosnetoses;
    }
    
    public void setIngresosnetoses(Set ingresosnetoses) {
        this.ingresosnetoses = ingresosnetoses;
    }
    public Set getValoreses() {
        return this.valoreses;
    }
    
    public void setValoreses(Set valoreses) {
        this.valoreses = valoreses;
    }
    public Set getTarjetacreditos() {
        return this.tarjetacreditos;
    }
    
    public void setTarjetacreditos(Set tarjetacreditos) {
        this.tarjetacreditos = tarjetacreditos;
    }
    public Set getReservases() {
        return this.reservases;
    }
    
    public void setReservases(Set reservases) {
        this.reservases = reservases;
    }
    public Set getPrestamos() {
        return this.prestamos;
    }
    
    public void setPrestamos(Set prestamos) {
        this.prestamos = prestamos;
    }
    public Set getRegvectors() {
        return this.regvectors;
    }
    
    public void setRegvectors(Set regvectors) {
        this.regvectors = regvectors;
    }
    public Set getRegcuentausers() {
        return this.regcuentausers;
    }
    
    public void setRegcuentausers(Set regcuentausers) {
        this.regcuentausers = regcuentausers;
    }
    public Set getCuentas() {
        return this.cuentas;
    }
    
    public void setCuentas(Set cuentas) {
        this.cuentas = cuentas;
    }
    public Set getCaptacions() {
        return this.captacions;
    }
    
    public void setCaptacions(Set captacions) {
        this.captacions = captacions;
    }



    /**
     * Se encarga de comparar dos ejercicios y darnos la variacion entre ellos ,
     * se le debe pasar la variacion maxima que toleraremos , asi como el numero
     * de Registros deseados, si el numero de registros es negativo entonces se
     * daran todos los registros
     *
     * @param comparable
     * @param minVariance
     * @param numberRegisters
     * @return regresa un mapeo que contiene <1 , <numCuenta,val1,val2,comp>>
     * <2,<numCuenta,val1,val2,comp>> asi hasta cumplir con los registros
     * requeridos
     *
     */
    public Map compareProjects(Regcuenta comparable, double minVariance, int numberRegisters,Collection<Cuenta> ownCuentasParam,Collection<Cuenta> otherCuentasParam) {
        // sacamos los valores de las cuentas y los comparamos
        List<Catalogocuenta> createQuery = DAO.createQuery(Catalogocuenta.class, null);
        Map<String, NDimensionVector> mapping = new HashMap<String, NDimensionVector>();
        Map<Double, NDimensionVector> sortly = new HashMap<Double, NDimensionVector>();
        Map<String, Cuenta> ownCuentas = mapCuentas(this,ownCuentasParam);
        Map<String, Cuenta> compareCuentas = mapCuentas(comparable,otherCuentasParam);
        for (Catalogocuenta count : createQuery) {
            String numberCount = count.getIdCatalogoCuenta().toString();
            Cuenta ownValue = ownCuentas.get(numberCount);
            Cuenta compareValue = compareCuentas.get(numberCount);
            Double value1 = ownValue == null ? Double.NaN : ownValue.getValor();
            Double value2 = compareValue == null ? Double.NaN : compareValue.getValor();
            Double compare = value1 == Double.NaN || value2 == Double.NaN ? Double.NaN : Math.abs((value2 / value1) - 1);
            mapping.put(numberCount, new NDimensionVector(value1, value2, compare));
            if (sortly.get(compare) == null) {
                sortly.put(compare, new NDimensionVector<Double>());
            }
            NDimensionVector get = sortly.get(compare);
            get.addValue(numberCount);
            sortly.put(compare, get);
        }
        //ordenamos por comparacion
        List<Double> sortedCompare = Util.sortDoubleValues(sortly.keySet());
        Map<Integer, NDimensionVector> answer = new HashMap<Integer, NDimensionVector>();
        int maxRegisters = numberRegisters < 0 ? sortedCompare.size() : numberRegisters;
        int actualRegisters = 0;
        int counter = 0;
        //empezamos a meter solo los registros que nececitamos
        while (maxRegisters > 0 && counter < sortedCompare.size()) {
            Double compareVal = sortedCompare.get(sortedCompare.size() - 1 - counter);
            if (compareVal >= minVariance || !compareVal.isNaN()) {
                NDimensionVector numberCounts = sortly.get(compareVal);
                NDimensionVector newVector = new NDimensionVector();
                List<String> values = numberCounts.getValues();
                for (String s : values) {
                    if (maxRegisters > 0) {
                        newVector.addValue(s);
                        newVector.addValues(mapping.get(s));
                        answer.put(actualRegisters, newVector);
                        maxRegisters--;
                        actualRegisters++;
                    }
                }
            }

            counter++;
        }
        return answer;
    }

    public static void main(String[] args) {
        List<Regcuenta> createQuery = DAO.createQuery(Regcuenta.class, null);
        Regcuenta get = createQuery.get(0);
//        Map<Integer, NDimensionVector> compareProjects = get.compareProjects(createQuery.get(1), 0.0, -1);
//        System.out.println(compareProjects.get(2).getValues());
    }

    @Override
    public String toString() {
        return String.valueOf(idRegCuenta);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private Map<String, Cuenta> mapCuentas(Regcuenta regCta,Collection<Cuenta> cuentas) {
        Map<String, Cuenta> ans = new HashMap<String, Cuenta>();
        for (Cuenta c : cuentas) {
            ans.put(c.getCatalogocuenta().getIdCatalogoCuenta().toString(), c);
        }
        return ans;
    }

    public Cuenta getCuenta(String numberCount,Collection<Cuenta> cuentas1) {
        for (Cuenta c : cuentas1) {
            if (c.getRegcuenta().getIdRegCuenta().toString().equals(this.getIdRegCuenta().toString()) && 
                    c.getCatalogocuenta().getIdCatalogoCuenta().toString().equals(numberCount)) {
                return c;
            }
        }
        return null;
    }

    public Cuenta getCuentaById(String id,Collection<Cuenta> cuentas1) {
        for (Cuenta c : cuentas1) {
            if (c.getRegcuenta().getIdRegCuenta().toString().equals(this.getIdRegCuenta().toString()) &&
                    c.getIdCuenta().toString().equals(id)) {
                return c;
            }
        }
        return null;
    }


}


