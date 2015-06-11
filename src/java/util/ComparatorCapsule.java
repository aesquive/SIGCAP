/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author desarrollo
 */
public class ComparatorCapsule implements Comparable<ComparatorCapsule>{
    
    private Comparable comparableKey;
    private Object  value;

    public ComparatorCapsule(Comparable comparableKey, Object value) {
        this.comparableKey = comparableKey;
        this.value = value;
    }
    
    

    /**
     * @return the comparableKey
     */
    public Comparable getComparableKey() {
        return comparableKey;
    }

    /**
     * @param comparableKey the comparableKey to set
     */
    public void setComparableKey(Comparable comparableKey) {
        this.comparableKey = comparableKey;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int compareTo(ComparatorCapsule o) {
        return comparableKey.compareTo(o.getComparableKey());
    }
    
    
}
