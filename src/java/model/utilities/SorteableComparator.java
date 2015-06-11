/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utilities;

import db.pojos.Cuenta;

/**
 *
 * @author desarrollo
 */
public class SorteableComparator implements Comparable<SorteableComparator>{

    private Double compareValue;
    private Cuenta firstCuenta;
    private Cuenta secondCuenta;
    private Double firstValue;
    private Double secondValue;
    
    SorteableComparator(Double compareValue, Cuenta firstCuenta, Cuenta secondCuenta, Double firstValue, Double secondValue) {
        this.compareValue=compareValue;
        this.firstCuenta=firstCuenta;
        this.secondCuenta=secondCuenta;
        this.firstValue=firstValue;
        this.secondValue=secondValue;
    }

    /**
     * @return the compareValue
     */
    public Double getCompareValue() {
        return compareValue;
    }

    /**
     * @param compareValue the compareValue to set
     */
    public void setCompareValue(Double compareValue) {
        this.compareValue = compareValue;
    }

    /**
     * @return the firstCuenta
     */
    public Cuenta getFirstCuenta() {
        return firstCuenta;
    }

    /**
     * @param firstCuenta the firstCuenta to set
     */
    public void setFirstCuenta(Cuenta firstCuenta) {
        this.firstCuenta = firstCuenta;
    }

    /**
     * @return the secondCuenta
     */
    public Cuenta getSecondCuenta() {
        return secondCuenta;
    }

    /**
     * @param secondCuenta the secondCuenta to set
     */
    public void setSecondCuenta(Cuenta secondCuenta) {
        this.secondCuenta = secondCuenta;
    }

    /**
     * @return the firstValue
     */
    public Double getFirstValue() {
        return firstValue;
    }

    /**
     * @param firstValue the firstValue to set
     */
    public void setFirstValue(Double firstValue) {
        this.firstValue = firstValue;
    }

    /**
     * @return the secondValue
     */
    public Double getSecondValue() {
        return secondValue;
    }

    /**
     * @param secondValue the secondValue to set
     */
    public void setSecondValue(Double secondValue) {
        this.secondValue = secondValue;
    }

    @Override
    public int compareTo(SorteableComparator o) {
        return this.getCompareValue().compareTo(o.getCompareValue());
    }

    public String toString(){
        return "compare "+getCompareValue()+" de "+firstValue+" y de "+secondValue;
    }
    
}
