
package util;

import db.pojos.Catalogocuenta;

/**
 *
 * @author Admin
 */
public class CompareObjectReport implements Comparable{
    private Catalogocuenta cuenta;

    public CompareObjectReport(Catalogocuenta cuenta, Double valorPrimero, Double valorSegundo) {
        this.cuenta = cuenta;
        this.valorPrimero = valorPrimero;
        this.valorSegundo = valorSegundo;
    }
    
    
    
    private Double valorPrimero;
    private Double valorSegundo;
    private Double razon;

    /**
     * @return the cuenta
     */
    public Catalogocuenta getCuenta() {
        return cuenta;
    }

    /**
     * @param cuenta the cuenta to set
     */
    public void setCuenta(Catalogocuenta cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * @return the valorPrimero
     */
    public Double getValorPrimero() {
        return valorPrimero;
    }

    /**
     * @param valorPrimero the valorPrimero to set
     */
    public void setValorPrimero(Double valorPrimero) {
        this.valorPrimero = valorPrimero;
    }

    /**
     * @return the valorSegundo
     */
    public Double getValorSegundo() {
        return valorSegundo;
    }

    /**
     * @param valorSegundo the valorSegundo to set
     */
    public void setValorSegundo(Double valorSegundo) {
        this.valorSegundo = valorSegundo;
    }

    /**
     * @return the razon
     */
    public Double getRazon() {
        if(valorPrimero==Double.NaN || valorSegundo==Double.NaN || valorPrimero==null || valorSegundo==null){
            return Double.NaN;
        }
        if(valorPrimero<=0 || valorSegundo<=0){
            double factorSuma=Math.abs(Math.min(valorPrimero, valorSegundo))+3;
            return Math.abs((valorPrimero+factorSuma)/(valorSegundo+factorSuma)-1);
        }
        return Math.abs(valorPrimero/valorSegundo-1);
    }

    /**
     * @param razon the razon to set
     */
    public void setRazon(Double razon) {
        this.razon = razon;
    }

    @Override
    public int compareTo(Object t) {
        CompareObjectReport comp=(CompareObjectReport)t;
        return this.getRazon().compareTo(comp.getRazon());
    }

    
}

