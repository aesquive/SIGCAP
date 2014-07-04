
package model.wrappers;

/**
 *
 * @author Admin
 */
public class CatalogoMinimoWrapper {

    private String cuenta;
    private Double valor;
    
    public CatalogoMinimoWrapper(String cuenta, Double valor) {
        this.cuenta = cuenta;
        this.valor = valor;
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
    
    
}
