
package model.wrappers;

/**
 *
 * @author Admin
 */
public class CatalogoMinimoWrapper {

    private String cuenta;
    private Double valor;
    private String moneda;
    
    public CatalogoMinimoWrapper(String cuenta, Double valor,String moneda) {
        this.cuenta = cuenta;
        this.valor = valor;
        this.moneda=moneda;
    }
    

    /**
     * @return the cuenta
     */
    public String getCuenta() {
        return cuenta;
    }

    /**
     * @param cuenta the cuenta to set
     */
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    
    
}
